import cv2
import numpy as np
import matplotlib.pyplot as plt
import math


def get_interest_points(image, feature_width):
    """
    Implement the Harris corner detector (See Szeliski 4.1.1) to start with.
    You can create additional interest point detector functions (e.g. MSER)
    for extra credit.

    If you're finding spurious interest point detections near the boundaries,
    it is safe to simply suppress the gradients / corners near the edges of
    the image.

    Useful in this function in order to (a) suppress boundary interest
    points (where a feature wouldn't fit entirely in the image, anyway)
    or (b) scale the image filters being used. Or you can ignore it.

    By default you do not need to make scale and orientation invariant
    local features.

    The lecture slides and textbook are a bit vague on how to do the
    non-maximum suppression once you've thresholded the cornerness score.
    You are free to experiment. For example, you could compute connected
    components and take the maximum value within each component.
    Alternatively, you could run a max() operator on each sliding window. You
    could use this to ensure that every interest point is at a local maximum
    of cornerness.

    Args:
    -   image: A numpy array of shape (m,n,c),
                image may be grayscale of color (your choice)
    -   feature_width: integer representing the local feature width in pixels.

    Returns:
    -   x: A numpy array of shape (N,) containing x-coordinates of interest points
    -   y: A numpy array of shape (N,) containing y-coordinates of interest points
    -   confidences (optional): numpy nd-array of dim (N,) containing the strength
            of each interest point
    -   scales (optional): A numpy array of shape (N,) containing the scale at each
            interest point
    -   orientations (optional): A numpy array of shape (N,) containing the orientation
            at each interest point
    """
    confidences, scales, orientations = None, None, None
    #############################################################################
    # TODO: YOUR HARRIS CORNER DETECTOR CODE HERE   
    #############################################################################
    G_low = cv2.getGaussianKernel(3,2)
    filtered_image = cv2.filter2D(image,-1,G_low)
    dy, dx = np.gradient(filtered_image)

    Ix2 = dx**2
    Ixy = dx*dy
    Iy2 = dy**2

    G_high = cv2.getGaussianKernel(9,2)
    dx2_w = cv2.filter2D(Ix2, -1, G_high)
    dxy_w = cv2.filter2D(Ixy, -1, G_high)
    dy2_w = cv2.filter2D(Iy2, -1, G_high)

    alpha = 0.07

    Response = dx2_w * dy2_w - dxy_w **2 -alpha * (dx2_w + dy2_w) **2

    
    #############################################################################
    #                             END OF YOUR CODE                              #
    #############################################################################
    
    #############################################################################
    # TODO: YOUR ADAPTIVE NON-MAXIMAL SUPPRESSION CODE HERE                     #
    # While most feature detectors simply look for local maxima in              #
    # the interest function, this can lead to an uneven distribution            #
    # of feature points across the image, e.g., points will be denser           #
    # in regions of higher contrast. To mitigate this problem, Brown,           #
    # Szeliski, and Winder (2005) only detect features that are both            #
    # local maxima and whose response value is significantly (10%)              #
    # greater than that of all of its neighbors within a radius r. The          #
    # goal is to retain only those points that are a maximum in a               #
    # neighborhood of radius r pixels. One way to do so is to sort all          #
    # points by the response strength, from large to small response.            #
    # The first entry in the list is the global maximum, which is not           #
    # suppressed at any radius. Then, we can iterate through the list           #
    # and compute the distance to each interest point ahead of it in            #
    # the list (these are pixels with even greater response strength).          #
    # The minimum of distances to a keypoint's stronger neighbors               #
    # (multiplying these neighbors by >=1.1 to add robustness) is the           #
    # radius within which the current point is a local maximum. We              #
    # call this the suppression radius of this interest point, and we           #
    # save these suppression radii. Finally, we sort the suppression            #
    # radii from large to small, and return the n keypoints                     #
    # associated with the top n suppression radii, in this sorted               #
    # orderself. Feel free to experiment with n, we used n=1500.                #
    #                                                                           #
    # See:                                                                      #
    # https://www.microsoft.com/en-us/research/wp-content/uploads/2005/06/cvpr05.pdf
    # or                                                                        #
    # https://www.cs.ucsb.edu/~holl/pubs/Gauglitz-2011-ICIP.pdf                 #
    #############################################################################

    # find local max
 
    n=21000
    limited_r = 1
    local_maximum = 1


    threshold = np.mean(Response)
    Response[Response < threshold] = 0.0
    ordered_R = np.sort(Response.ravel())[::-1][:n]

    interest_points = []
    x = []
    y = []

    # global_maximum
    idx = np.where(Response == ordered_R[0])
    interest_points.append([int(idx[1]), int(idx[0]), min(image.shape[0], image.shape[1])])
    x.append(int(idx[1]))
    y.append(int(idx[0]))

    j = 1
    for i in range(1, len(ordered_R)):
        idx = np.where(Response == ordered_R[i])
        if (np.size(idx, 1) == 1):
            x.append(int(idx[1]))
            y.append(int(idx[0]))

            curr_x, curr_y = int(x[j]), int(y[j])
            prev_x, prev_y = np.array(x[:j]), np.array(y[:j])

            dis = np.sqrt((prev_x - curr_x) ** 2 + (prev_y - curr_y) ** 2)
            radii = int(np.amin(dis))
            j += 1
            Response[curr_y, curr_x] = 0

            if radii < limited_r or curr_x - radii < 0 or curr_x + radii > image.shape[
                1] or curr_y - radii < 0 or curr_y + radii > image.shape[0]:  # reach the boundary
                continue
            elif ordered_R[i] > local_maximum * Response[curr_y - radii:curr_y + radii,
                                                curr_x - radii:curr_x + radii].max():  # make sure curr_R is the maximum in the neighbor
                interest_points.append([curr_x, curr_y, radii])

    interest_points = np.array(interest_points)
    interest_points = interest_points[interest_points[:, 2].argsort()][::-1]
    interest_points = interest_points[0:1500]
    x = interest_points[:, 0]
    y = interest_points[:, 1]

    x = np.array(x)
    y = np.array(y)
    return x,y, confidences, scales, orientations
    #############################################################################
    #                             END OF YOUR CODE                              #
    #############################################################################


