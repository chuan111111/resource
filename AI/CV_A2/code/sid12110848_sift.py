import numpy as np
import cv2
import math


def get_features(image, x, y, feature_width, scales=None):
    """
    To start with, you might want to simply use normalized patches as your
    local feature. This is very simple to code and works OK. However, to get
    full credit you will need to implement the more effective SIFT descriptor
    (See Szeliski 4.1.2 or the original publications at
    http://www.cs.ubc.ca/~lowe/keypoints/)

    Your implementation does not need to exactly match the SIFT reference.
    Here are the key properties your (baseline) descriptor should have:
    (1) a 4x4 grid of cells, each feature_width/4. It is simply the
        terminology used in the feature literature to describe the spatial
        bins where gradient distributions will be described.
    (2) each cell should have a histogram of the local distribution of
        gradients in 8 orientations. Appending these histograms together will
        give you 4x4 x 8 = 128 dimensions.
    (3) Each feature should be normalized to unit length.

    You do not need to perform the interpolation in which each gradient
    measurement contributes to multiple orientation bins in multiple cells
    As described in Szeliski, a single gradient measurement creates a
    weighted contribution to the 4 nearest cells and the 2 nearest
    orientation bins within each cell, for 8 total contributions. This type
    of interpolation probably will help, though.

    You do not have to explicitly compute the gradient orientation at each
    pixel (although you are free to do so). You can instead filter with
    oriented filters (e.g. a filter that responds to edges with a specific
    orientation). All of your SIFT-like feature can be constructed entirely
    from filtering fairly quickly in this way.

    You do not need to do the normalize -> threshold -> normalize again
    operation as detailed in Szeliski and the SIFT paper. It can help, though.

    Another simple trick which can help is to raise each element of the final
    feature vector to some power that is less than one.

    Args:
    -   image: A numpy array of shape (m,n) or (m,n,c). can be grayscale or color, your choice
    -   x: A numpy array of shape (k,), the x-coordinates of interest points
    -   y: A numpy array of shape (k,), the y-coordinates of interest points
    -   feature_width: integer representing the local feature width in pixels.
            You can assume that feature_width will be a multiple of 4 (i.e. every
                cell of your local SIFT-like feature will have an integer width
                and height). This is the initial window size we examine around
                each keypoint.
    -   scales: Python list or tuple if you want to detect and describe features
            at multiple scales

    You may also detect and describe features at particular orientations.

    Returns:
    -   fv: A numpy array of shape (k, feat_dim) representing a feature vector.
            "feat_dim" is the feature_dimensionality (e.g. 128 for standard SIFT).
            These are the computed features.
    """

    #############################################################################
    # TODO: YOUR CODE HERE                                                      #
    # If you choose to implement rotation invariance, enabling it should not    #
    # decrease your matching accuracy.                                          #
    #############################################################################
    assert image.ndim == 2, 'Image must be grayscale'
    
    num_keypoints = len(x)
    fv = np.zeros((num_keypoints, 128))  # Initialize feature vectors
    
    # Pad the image to handle edge cases
    pad_width = feature_width // 2 
    padded_image = np.pad(image, ((pad_width, pad_width), (pad_width, pad_width)), mode='constant')
    
    # Compute image gradients
    dx = cv2.Sobel(padded_image, cv2.CV_64F, 1, 0, ksize=5)
    dy = cv2.Sobel(padded_image, cv2.CV_64F, 0, 1, ksize=5)
    
    # Loop over all interest points
    for i in range(num_keypoints):
        # Get coordinates of current interest point
        cx, cy = x[i] + pad_width, y[i] + pad_width  # Adjust for padding
        
        # Extract patch around interest point
        patch = padded_image[cy - pad_width : cy + pad_width, cx - pad_width : cx + pad_width]
        
        # Compute gradient magnitude and orientation
        magnitude = np.sqrt(dx[cy - pad_width : cy + pad_width, cx - pad_width : cx + pad_width] ** 2 +
                            dy[cy - pad_width : cy + pad_width, cx - pad_width : cx + pad_width] ** 2)
        orientation = np.arctan2(dy[cy - pad_width : cy + pad_width, cx - pad_width : cx + pad_width],
                                  dx[cy - pad_width : cy + pad_width, cx - pad_width : cx + pad_width])
        
        # Compute histogram for each cell in 4x4 grid
        cell_size = feature_width // 4
        for j in range(4):
            for k in range(4):
                cell_histogram = np.zeros(8)
                for m in range(cell_size):
                    for n in range(cell_size):
                        angle = orientation[j * cell_size + m, k * cell_size + n]
                        bin_index = int((angle + math.pi) * 4 / math.pi) % 8
                        cell_histogram[bin_index] += magnitude[j * cell_size + m, k * cell_size + n]
                
                # Append cell histogram to feature vector
                fv[i, (j * 4 + k) * 8 : (j * 4 + k + 1) * 8] = cell_histogram
        
        # Normalize feature vector
        fv[i] /= np.linalg.norm(fv[i])
    
    return fv

    #############################################################################
    #                             END OF YOUR CODE                              #
    #############################################################################
   

     # Placeholder implementation using normalized patches as local features
 