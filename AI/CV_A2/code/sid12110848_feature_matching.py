import numpy as np


def match_features(features1, features2, x1, y1, x2, y2):
    """
    This function does not need to be symmetric (e.g. it can produce
    different numbers of matches depending on the order of the arguments).

    To start with, simply implement the "ratio test", equation 4.18 in
    section 4.1.3 of Szeliski. There are a lot of repetitive features in
    these images, and all of their descriptors will look similar. The
    ratio test helps us resolve this issue (also see Figure 11 of David
    Lowe's IJCV paper).

    For extra credit you can implement various forms of spatial/geometric
    verification of matches, e.g. using the x and y locations of the features.

    Args:
    -   features1: A numpy array of shape (n,feat_dim) representing one set of
            features, where feat_dim denotes the feature dimensionality
    -   features2: A numpy array of shape (m,feat_dim) representing a second set
            features (m not necessarily equal to n)
    -   x1: A numpy array of shape (n,) containing the x-locations of features1
    -   y1: A numpy array of shape (n,) containing the y-locations of features1
    -   x2: A numpy array of shape (m,) containing the x-locations of features2
    -   y2: A numpy array of shape (m,) containing the y-locations of features2

    Returns:
    -   matches: A numpy array of shape (k,2), where k is the number of matches.
            The first column is an index in features1, and the second column is
            an index in features2
    -   confidences: A numpy array of shape (k,) with the real valued confidence for
            every match

    'matches' and 'confidences' can be empty e.g. (0x2) and (0x1)
    """
    #############################################################################
    # TODO: YOUR CODE HERE                                                        #
#     #############################################################################

    match_indices = []
    confidences = []

    # Compute pairwise distances between features1 and features2
    distances = np.linalg.norm(features1[:, np.newaxis] - features2, axis=2)

    # Iterate over each feature in features1
    for i, (f1, x1_, y1_) in enumerate(zip(features1, x1, y1)):
        # Find the two closest matches for the current feature in features2
        closest_indices = np.argsort(distances[i])[:2]
        closest_distances = distances[i, closest_indices]

        # Compute the distance ratio
        distance_ratio = closest_distances[0] / closest_distances[1]

        # Apply ratio test (equation 4.18)
        if distance_ratio < 0.7:  # Adjust threshold as needed
            match_indices.append([i, closest_indices[0]])
            confidences.append(distance_ratio)

    # Convert lists to numpy arrays
    matches = np.array(match_indices)
    confidences = np.array(confidences)

    return matches, confidences

    #############################################################################
    #                             END OF YOUR CODE                              #
    #############################################################################
   
