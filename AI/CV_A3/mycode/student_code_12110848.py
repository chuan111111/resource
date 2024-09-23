import cv2
import numpy as np
import pickle
from utils import load_image, load_image_gray
import cyvlfeat as vlfeat
import sklearn.metrics.pairwise as sklearn_pairwise
from sklearn.svm import LinearSVC
from IPython.core.debugger import set_trace
# from PIL import Image
import scipy.spatial.distance as distance
from cyvlfeat.sift.dsift import dsift
from cyvlfeat.kmeans import kmeans
from time import time
from sklearn.metrics import pairwise_distances
from sklearn.cluster import KMeans
from scipy.spatial.distance import cdist
from cyvlfeat.sift.dsift import dsift
from cyvlfeat.kmeans import kmeans, kmeans_quantize
from tqdm import tqdm
from collections import Counter
from sklearn.cluster import KMeans
from skimage.feature import daisy


def get_tiny_images(image_paths):
    """
    This feature is inspired by the simple tiny images used as features in
    80 million tiny images: a large dataset for non-parametric object and
    scene recognition. A. Torralba, R. Fergus, W. T. Freeman. IEEE
    Transactions on Pattern Analysis and Machine Intelligence, vol.30(11),
    pp. 1958-1970, 2008. http://groups.csail.mit.edu/vision/TinyImages/

    To build a tiny image feature, simply resize the original image to a very
    small square resolution, e.g. 16x16. You can either resize the images to
    square while ignoring their aspect ratio or you can crop the center
    square portion out of each image. Making the tiny images zero mean and
    unit length (normalizing them) will increase performance modestly.

    Useful functions:
    -   cv2.resize
    -   use load_image(path) to load a RGB images and load_image_gray(path) to
        load grayscale images

    Args:
    -   image_paths: list of N elements containing image paths

    Returns:
    -   feats: N x d numpy array of resized and then vectorized tiny images
              e.g. if the images are resized to 16x16, d would be 256
    """
    # dummy feats variable
    feats = []

    #############################################################################
    # TODO: YOUR CODE HERE                                                      #
    #############################################################################

#     for i in range(len(image_paths)):
#         image = load_image_gray(image_paths[i])
#         image = cv2.resize(image, (16,16))

#         ##print(image)
        
#         Ir = image.flatten()
#         Izm = Ir - np.mean(Ir)
#         Iul = Izm/np.max(np.abs(Izm))
#         feats.append(Iul)

  
#     feats = np.array(feats)
    for path in image_paths:
        # Load image in grayscale
        image = cv2.imread(path, cv2.IMREAD_GRAYSCALE)
        
        # Resize image to 16x16
        tiny_image = cv2.resize(image, (16, 16))
        
        # Flatten the image
        tiny_image_flat = tiny_image.flatten()
        
        # Make the tiny image zero mean and unit length (normalize)
        tiny_image_flat = (tiny_image_flat - np.mean(tiny_image_flat)) / np.std(tiny_image_flat)
        
        # Append the flattened tiny image to feats
        feats.append(tiny_image_flat)
    
    # Convert feats to numpy array
    feats = np.array(feats)        

    #############################################################################
    #                             END OF YOUR CODE                              #
    #############################################################################

    return feats


def build_vocabulary(image_paths, vocab_size):
    """
    This function will sample SIFT descriptors from the training images,
    cluster them with kmeans, and then return the cluster centers.

    Useful functions:
    -   Use load_image(path) to load RGB images and load_image_gray(path) to load
            grayscale images
    -   frames, descriptors = vlfeat.sift.dsift(img)
          http://www.vlfeat.org/matlab/vl_dsift.html
            -  frames is a N x 2 matrix of locations, which can be thrown away
            here (but possibly used for extra credit in get_bags_of_sifts if
            you're making a "spatial pyramid").
            -  descriptors is a N x 128 matrix of SIFT features
          Note: there are step, bin size, and smoothing parameters you can
          manipulate for dsift(). We recommend debugging with the 'fast'
          parameter. This approximate version of SIFT is about 20 times faster to
          compute. Also, be sure not to use the default value of step size. It
          will be very slow and you'll see relatively little performance gain
          from extremely dense sampling. You are welcome to use your own SIFT
          feature code! It will probably be slower, though.
    -   cluster_centers = vlfeat.kmeans.kmeans(X, K)
            http://www.vlfeat.org/matlab/vl_kmeans.html
              -  X is a N x d numpy array of sampled SIFT features, where N is
                 the number of features sampled. N should be pretty large!
              -  K is the number of clusters desired (vocab_size)
                 cluster_centers is a K x d matrix of cluster centers. This is
                 your vocabulary.

    Args:
    -   image_paths: list of image paths.
    -   vocab_size: size of vocabulary

    Returns:
    -   vocab: This is a vocab_size x d numpy array (vocabulary). Each row is a
        cluster center / visual word
    """
    # Load images from the training set. To save computation time, you don't
    # necessarily need to sample from all images, although it would be better
    # to do so. You can randomly sample the descriptors from each image to save
    # memory and speed up the clustering. Or you can simply call vl_dsift with
    # a large step size here, but a smaller step size in get_bags_of_sifts.
    #
    # For each loaded image, get some SIFT features. You don't have to get as
    # many SIFT features as you will in get_bags_of_sift, because you're only
    # trying to get a representative sample here.
    #
    # Once you have tens of thousands of SIFT features from many training
    # images, cluster them with kmeans. The resulting centroids are now your
    # visual word vocabulary.

    # length of the SIFT descriptors that you are going to compute.
    dim = 128
    vocab = np.zeros((vocab_size, dim))

    #############################################################################
    # TODO: YOUR CODE HERE                                                      #
    #############################################################################

    # total_SIFT_features = np.zeros((20*len(image_paths), dim))
    # index = 0

    # for i in range(len(image_paths)):
    #     image = load_image_gray(image_paths[i]).astype('float32')

    #     [locations, SIFT_features] = vlfeat.sift.dsift(image,fast=True,step=15)

    #     rand_permutation = np.random.permutation(SIFT_features.shape[0])

    #     for j in range(20): 
    #             k = rand_permutation[j]
    #             total_SIFT_features[j+index, :] = SIFT_features[k, :]
    #     index = index + 20

    # vocab = vlfeat.kmeans.kmeans(total_SIFT_features.astype('float32'), vocab_size)
    # sift_descriptors = []

    # # Loop through each image path and extract SIFT descriptors
    # print(len(image_paths))
    # for path in image_paths:
    #     # Load the image in grayscale
    #     image = cv2.imread(path, cv2.IMREAD_GRAYSCALE)

    #     # Extract SIFT descriptors using DAISY (a variant of SIFT)
    #     desc = daisy(image)

    #     # Flatten the descriptors
    #     desc_flat = desc.reshape(-1, desc.shape[-1])

    #     # Append the descriptors to the list
    #     sift_descriptors.append(desc_flat)

    # # Convert the list of descriptors to a single numpy array
    # sift_descriptors = np.vstack(sift_descriptors)

    # # Perform KMeans clustering on the descriptors to obtain visual words (vocabulary)
    # kmeans = KMeans(n_clusters=vocab_size)
    # kmeans.fit(sift_descriptors)
    # vocab = kmeans.cluster_centers_
    sampled_features = []

    # Sample SIFT descriptors from the training images
    for path in image_paths:
        # Load and convert image to grayscale
        image = load_image_gray(path).astype('float32')

        # Compute SIFT features for the image
        _, sift_features = vlfeat.sift.dsift(image, fast=True, step=15)

        # Randomly sample 20 SIFT features from the image
        num_features = min(20, sift_features.shape[0])
        rand_indices = np.random.choice(sift_features.shape[0], size=num_features, replace=False)
        sampled_features.append(sift_features[rand_indices])

    # Concatenate sampled features from all images
    sampled_features = np.concatenate(sampled_features, axis=0)

    # Cluster the sampled features using kmeans
    vocab = vlfeat.kmeans.kmeans(sampled_features.astype('float32'), vocab_size)


    #############################################################################
    #                             END OF YOUR CODE                              #
    #############################################################################

    return vocab


def get_bags_of_sifts(image_paths, vocab_filename):
    """
    This feature representation is described in the handout, lecture
    materials, and Szeliski chapter 14.
    You will want to construct SIFT features here in the same way you
    did in build_vocabulary() (except for possibly changing the sampling
    rate) and then assign each local feature to its nearest cluster center
    and build a histogram indicating how many times each cluster was used.
    Don't forget to normalize the histogram, or else a larger image with more
    SIFT features will look very different from a smaller version of the same
    image.

    Useful functions:
    -   Use load_image(path) to load RGB images and load_image_gray(path) to load
            grayscale images
    -   frames, descriptors = vlfeat.sift.dsift(img)
            http://www.vlfeat.org/matlab/vl_dsift.html
          frames is a M x 2 matrix of locations, which can be thrown away here
            (but possibly used for extra credit in get_bags_of_sifts if you're
            making a "spatial pyramid").
          descriptors is a M x 128 matrix of SIFT features
            note: there are step, bin size, and smoothing parameters you can
            manipulate for dsift(). We recommend debugging with the 'fast'
            parameter. This approximate version of SIFT is about 20 times faster
            to compute. Also, be sure not to use the default value of step size.
            It will be very slow and you'll see relatively little performance
            gain from extremely dense sampling. You are welcome to use your own
            SIFT feature code! It will probably be slower, though.
    -   assignments = vlfeat.kmeans.kmeans_quantize(data, vocab)
            finds the cluster assigments for features in data
              -  data is a M x d matrix of image features
              -  vocab is the vocab_size x d matrix of cluster centers
              (vocabulary)
              -  assignments is a Mx1 array of assignments of feature vectors to
              nearest cluster centers, each element is an integer in
              [0, vocab_size)

    Args:
    -   image_paths: paths to N images
    -   vocab_filename: Path to the precomputed vocabulary.
            This function assumes that vocab_filename exists and contains an
            vocab_size x 128 ndarray 'vocab' where each row is a kmeans centroid
            or visual word. This ndarray is saved to disk rather than passed in
            as a parameter to avoid recomputing the vocabulary every run.

    Returns:
    -   image_feats: N x d matrix, where d is the dimensionality of the
            feature representation. In this case, d will equal the number of
            clusters or equivalently the number of entries in each image's
            histogram (vocab_size) below.
    """
    # load vocabulary
    with open(vocab_filename, 'rb') as f:
        vocab = pickle.load(f)

    # dummy features variable
    
    
    #############################################################################
    # TODO: YOUR CODE HERE                                                      #
    #############################################################################
    feats  = []
    vocab_size = vocab.shape[0]

    for i in range(len(image_paths)):
        image = load_image_gray(image_paths[i]).astype('float32')
        [locations, SIFT_features] = vlfeat.sift.dsift(image,fast=True,step=10)
        SIFT_features = SIFT_features.astype('float32')

        Hist = np.zeros(vocab_size)
        D = sklearn_pairwise.pairwise_distances(SIFT_features, vocab)
        for j in D:
                closet = np.argmin(a=j,axis=0)
                Hist[closet] +=1

        Hist = Hist / np.linalg.norm(Hist)

        feats.append(Hist)
    feats = np.array(feats)
    return feats
 

    # Compute SIFT features for each image
 
    # for path in image_paths:
    #     image = load_image_gray(path).astype('float32')
    #     _, sift_features = vlfeat.sift.dsift(image, fast=True, step=10)
    #     sift_features = sift_features.astype('float32')

    #     # Use K-means to assign each feature to nearest cluster center
    #     nearest_clusters = KMeans(n_clusters=len(vocab), n_init=1).fit_predict(sift_features)

    #     # Compute histogram of cluster assignments
    #     hist, _ = np.histogram(nearest_clusters, bins=np.arange(len(vocab) + 1), density=True)

    #     # Normalize histogram
    #     hist /= np.linalg.norm(hist)

    #     # Add histogram to features list
    #     feats.append(hist)

    # # Convert features list to numpy array
    # feats = np.array(feats)

    # return feats
  
  

    #############################################################################
    #                             END OF YOUR CODE                              #
    #############################################################################
    


def nearest_neighbor_classify(train_image_feats, train_labels, test_image_feats,
                              metric='euclidean'):
    """
    This function will predict the category for every test image by finding
    the training image with most similar features. Instead of 1 nearest
    neighbor, you can vote based on k nearest neighbors which will increase
    performance (although you need to pick a reasonable value for k).

    Useful functions:
    -   D = sklearn_pairwise.pairwise_distances(X, Y)
          computes the distance matrix D between all pairs of rows in X and Y.
            -  X is a N x d numpy array of d-dimensional features arranged along
            N rows
            -  Y is a M x d numpy array of d-dimensional features arranged along
            N rows
            -  D is a N x M numpy array where d(i, j) is the distance between row
            i of X and row j of Y

    Args:
    -   train_image_feats:  N x d numpy array, where d is the dimensionality of
            the feature representation
    -   train_labels: N element list, where each entry is a string indicating
            the ground truth category for each training image
    -   test_image_feats: M x d numpy array, where d is the dimensionality of the
            feature representation. You can assume N = M, unless you have changed
            the starter code
    -   metric: (optional) metric to be used for nearest neighbor.
            Can be used to select different distance functions. The default
            metric, 'euclidean' is fine for tiny images. 'chi2' tends to work
            well for histograms

    Returns:
    -   test_labels: M element list, where each entry is a string indicating the
            predicted category for each testing image
    """
    test_labels = []

    #############################################################################
    # TODO: YOUR CODE HERE                                                      #
    #############################################################################

#     N = train_image_feats.shape[0]
#     M = test_image_feats.shape[0]

#     D = sklearn_pairwise.pairwise_distances(train_image_feats, test_image_feats)
#     row_min = 0
#     for i in range(0, M):
#         row_min = np.min(D[i,:])
#         for j in range(0, N):
#           if (D[i,j] == row_min):
#                 test_labels.append(train_labels[j])
#                 break
    

    # distances = pairwise_distances(test_image_feats, train_image_feats, metric=metric)

    # # Find the index of the nearest training image for each test image
    # nearest_indices = np.argmin(distances, axis=1)

    # # Get the corresponding labels for the nearest training images
    # test_labels = [train_labels[idx] for idx in nearest_indices]

    distances = pairwise_distances(test_image_feats, train_image_feats, metric=metric)

    # Find the k nearest neighbors for each test image
   # nearest_indices = np.argsort(distances, axis=1)[:, :19]
    nearest_indices = np.argsort(distances, axis=1)[:, :13]
    # Get the corresponding labels for the nearest training images
    for indices in nearest_indices:
        nearest_labels = [train_labels[idx] for idx in indices]
        # Use simple majority voting to determine the predicted label
        predicted_label = Counter(nearest_labels).most_common(1)[0][0]
        test_labels.append(predicted_label)

    
    #############################################################################
    #                             END OF YOUR CODE                              #
    #############################################################################

    return test_labels


def svm_classify(train_image_feats, train_labels, test_image_feats):
    """
    This function will train a linear SVM for every category (i.e. one vs all)
    and then use the learned linear classifiers to predict the category of
    every test image. Every test feature will be evaluated with all 15 SVMs
    and the most confident SVM will "win". Confidence, or distance from the
    margin, is W*X + B where '*' is the inner product or dot product and W and
    B are the learned hyperplane parameters.

    Useful functions:
    -   sklearn LinearSVC
          http://scikit-learn.org/stable/modules/generated/sklearn.svm.LinearSVC.html
    -   svm.fit(X, y)
    -   set(l)

    Args:
    -   train_image_feats:  N x d numpy array, where d is the dimensionality of
            the feature representation
    -   train_labels: N element list, where each entry is a string indicating the
            ground truth category for each training image
    -   test_image_feats: M x d numpy array, where d is the dimensionality of the
            feature representation. You can assume N = M, unless you have changed
            the starter code
    Returns:
    -   test_labels: M element list, where each entry is a string indicating the
            predicted category for each testing image
    """
    # categories
    categories = list(set(train_labels))

    # construct 1 vs all SVMs for each category
    svms = {cat: LinearSVC(random_state=0, tol=1e-3, loss='hinge', C=5)
            for cat in categories}

    test_labels = []



    #############################################################################
    # TODO: YOUR CODE HERE                                                      #
    #############################################################################

#     raise NotImplementedError('`svm_classify` function in ' +
#                               '`student_code.py` needs to be implemented')

#     num_categories = len(categories)
#     true_label = np.zeros(len(train_labels))
#     W = []
#     C = []

#     for i in range(num_categories):
#       for j in range(len(train_labels)):
#         true_label[j] = (categories[i]==train_labels[j])
#       binary_label = np.ones(len(true_label))*-1
#       for k in range(len(binary_label)):
#         if(true_label[k]==1):
#           binary_label[k] = 1

#       svms[categories[i]].fit(train_image_feats,binary_label,sample_weight=None)
#     # print(svms[categories[i]].coef_)

#       W.append(svms[categories[i]].coef_)
#       C.append(svms[categories[i]].intercept_)
  
#     W = np.array(W)
#     C = np.array(C)


#     num_test_image_feats = test_image_feats.shape[0]
#     for i in range(num_test_image_feats):
#         confidence = []
#         for j in range(num_categories):
#                 confidence.append(np.dot(W[j,0,:], test_image_feats[i, :]) + C[j, :])
#         max_conf = max(confidence)
#         for ind in range(num_categories):
#                 if(confidence[ind] == max_conf):
#                         test_labels.append(categories[ind])
    for cat, svm in svms.items():
        # Create binary labels for the current category
        binary_labels = [1 if label == cat else 0 for label in train_labels]
        # Train the SVM for the current category
        svm.fit(train_image_feats, binary_labels)

    # Predict using trained SVMs
   
    for test_feat in test_image_feats:
        best_score = -float('inf')
        best_label = None
        for cat, svm in svms.items():
            # Compute confidence score for the current category
            score = svm.decision_function([test_feat])[0]
            if score > best_score:
                best_score = score
                best_label = cat
        test_labels.append(best_label)

    #############################################################################
    #                             END OF YOUR CODE                              #
    #############################################################################

    return test_labels
