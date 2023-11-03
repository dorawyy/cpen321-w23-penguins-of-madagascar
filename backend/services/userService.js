import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();

export const createUser = async (userData) => {
    console.log(userData);
    return await prisma.User.create({
        data: userData,
    });
};

export const getUser = async (userId) => {
    const uid = parseInt(userId);

    if (isNaN(uid)) {
        return res.status(400).json({ message: "Error: getUser | user ID is not int." });
    }

    return await prisma.User.findUnique({
        where: {
            id: uid,
            isDeleted: false,
            isActive: true,
        },
    });
};

export const getUserByEmail = async (email) => {
    try {
        const user = await prisma.User.findFirst({
            where: { 
                email: email,
                isDeleted: false,
                isActive: true,
            },
        });

        if (user) return user;

        const createdUser = await prisma.User.create({
            data: { 
                name: email,
                email: email,
                avatar: 'none',
                biography: 'none',
                reliabilityScore: 100,
            }
        });

        return createdUser;

    } catch (err) {
        throw err;
    }
};

export const updateUser = async (userId, updateData) => {
    return await prisma.User.update({
        where: { id: Number(userId) },
        data: updateData,
    });
};

export const updateUserBux = async (userId, polarity, amount) => {
    return await prisma.User.update({
        where: { id: Number(userId) },
        data: {
            mapBux: (polarity) ? { increment: amount } : { decrement: amount }
        },
        select: { mapBux: true }
    })
}

export const deleteUser = async (userId) => {
    return await prisma.User.update({
        where: { id: Number(userId) },
        data: { isDeleted: true },  // Soft-delete user.
    });
};

export const listUsers = async () => {
    return await prisma.User.findMany({
        where: {
            isDeleted: false,
            isActive: true
        }
    });
};

export const getUserReliabilityScore = async (userId) => {
    try {
        // Query for Pois reviewed by the user
        const reviewsByUser = await prisma.Review.findMany({
            where: {
                userId: Number(userId),
                isDeleted: false,
            }
        });
        if(reviewsByUser.length < 1) return 100;

        // iterate through every single poi reviewed by user
        let totalReviews = reviewsByUser.length;
        let distOfReview = new Array();
        for(const review of reviewsByUser){
            let dist = await distFromSafeZone(review.poiId, review.rating);
            distOfReview.push(dist);
        }
        let sumDist = reviewsByUser.reduce((sum, dist) => sum += dist, 0);
        let meanDist = (sumDist === 0) ? 0 : parseInt(sumDist / totalReviews);
        return 100 - meanDist;
    } catch (err) {
        throw err; 
    }
};

const distFromSafeZone = async (poiId, rating) => {
    const poiReviews = await prisma.Review.findMany({
        where: {
            poiId: poiId,
            isDeleted: false,
        }
    });
    const poiRatings = poiReviews.map(review => review.rating);
    if(poiRatings.length < 4) return 0;

    // find mean
    let sumRating = poiRatings.reduce((sum, rating) => sum += rating, 0);
    let numRatings = poiRatings.length;
    let mean = sumRating / numRatings;

    // find stdev
    let sumDist = poiRatings.reduce((sum, rating) => sum += Math.pow((rating - mean), 2), 0);
    let stdev = Math.pow((sumDist / numRatings), 0.5);

    let max = mean + stdev;
    let min = mean - stdev;

    if(rating > max) return rating - max;
    else if(rating < min) return min - rating;
    return 0;
}
