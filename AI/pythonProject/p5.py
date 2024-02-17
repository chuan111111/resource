import numpy as np


class MyLogisticRegression:
    def __init__(self):
        """
        Class constructor.

        :attr beta: weights vector of logistic regression.
        """
        self.beta = None

    def fx(self, X):
        """
        Calculate the value of f(x) given x.

        :param X: numpy.ndarray with a shape of (n, d), input data.
        :return:
            fx_value: numpy.ndarray with a length of n, output of f(x).
        """
        # TODO 1: complete the calculation process of f(x)
        fx_value = 1 / (1 + np.exp(-X @ self.beta))

        return fx_value

    def loss(self, fx_value, y):
        """
        Calculate the loss function given the calculated value f(x) and the true label y.

        :param fx_value: numpy.ndarray with a length of n,
                         a vector of hypothesis function values on these samples, which is the output of the function fx
        :param y: numpy.ndarray with a length of n,
                  a vector of the true labels of these samples
        :return:
            CELoss: a float value of the cross-entropy loss.
        """
        # TODO 2: complete the loss function calculation
        n = len(y)
        loss = -1 / n * np.sum(y * np.log(fx_value) + (1 - y) * np.log(1 - fx_value))

        return loss

    def fit(self, X, y, n_iters=100, alpha=0.01):
        """
        Train the model using gradient descent

        :param X: numpy.ndarray with a shape of (n*d), input data.
        :param y: numpy.ndarray with a length of n, the true labels of these samples
        :param n_iters: int, number of iterations
        :param alpha: float, learning rate
        :return:
            CELoss_list: list with a length of n_iters+1,
                         contains the loss values before the gradient descent and after the gradient descent.
        """

        n, d = X.shape

        self.beta = np.zeros(d + 1)
        # the first element in X_ is 1
        X_ = np.column_stack([np.ones(n), X])
        CELoss_list = []

        for i in range(n_iters):
            # TODO 3: update self.beta
            gradient = X_.T @ (self.fx(X_) - y) / n
            self.beta -= alpha * gradient
            CELoss_list.append(self.loss(self.fx(X_), y))

        CELoss_list.append(self.loss(self.fx(X_), y))
        return CELoss_list

    def predict(self, X):
        """
        Predict the labels of input instances.

        :param X: numpy.ndarray with a shape of (n*d), input data.
        :return:
            y_hat: numpy.ndarray with a length of n, the predicted labels of these samples
        """
        # TODO 4: predict the labels of the input data X and return the labels y_hat.
        fx_value = self.fx(np.column_stack([np.ones(X.shape[0]), X]))
        y_hat = (fx_value >= 0.5).astype(int)

        return y_hat
import matplotlib.pyplot as plt
import numpy as np
from sklearn import datasets

# load data
iris = datasets.load_iris()
X = iris.data[:, [2, 3]]
y = iris.target
y_mask = (y == 0) | (y == 1)
X = X[y_mask]
y = y[y_mask]

# plot the data
for idx, class_ in enumerate(np.unique(y)):
    plt.scatter(x=X[y == class_, 0], y=X[y == class_, 1],
                alpha=0.8, label="class {}".format(class_),
                edgecolor='black')
plt.legend()
plt.show()

from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

# split data into train and test datasets
X_train, X_test, y_train, y_test = train_test_split( X, y, test_size=0.3, random_state=42, stratify=y)

# standarize varibles by removing the mean and scaling to unit variance
sc = StandardScaler()
sc.fit(X_train)
X_train_std = sc.transform(X_train)
X_test_std = sc.transform(X_test)

# initialize and train the model
my_model = MyLogisticRegression()
losses = my_model.fit(X_train_std, y_train)
for i, loss in enumerate(losses):
    if i%10 ==0:
        print("Step {}: Cross-Entropy Loss: {}".format(i, loss))

from sklearn.metrics import accuracy_score

y_pred = my_model.predict(X_test_std)
acc = accuracy_score(y_test, y_pred)
print("The accuracy of your model is {}".format(acc))

import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap


def plot_decision_regions(X, y, classifier,
                          resolution=0.02):
    # setup marker generator and color map
    markers = ('s', 'x', 'o', '^', 'v')
    colors = ('red', 'blue', 'lightgreen', 'gray', 'cyan')
    cmap = ListedColormap(colors[:len(np.unique(y))])
    # plot the decision surface
    x1_min, x1_max = X[:, 0].min() - 1, X[:, 0].max() + 1
    x2_min, x2_max = X[:, 1].min() - 1, X[:, 1].max() + 1
    xx1, xx2 = np.meshgrid(np.arange(x1_min, x1_max, resolution),
                           np.arange(x2_min, x2_max, resolution))
    Z = classifier.predict(np.array([xx1.ravel(), xx2.ravel()]).T)
    Z = Z.reshape(xx1.shape)
    plt.contourf(xx1, xx2, Z, alpha=0.3, cmap=cmap)
    plt.xlim(xx1.min(), xx1.max())
    plt.ylim(xx2.min(), xx2.max())
    for idx, class_ in enumerate(np.unique(y)):
        plt.scatter(x=X[y == class_, 0], y=X[y == class_, 1],
                    alpha=0.8, c=colors[idx],
                    marker=markers[idx], label="class {}".format(class_),
                    edgecolor='black')

        plt.legend()


# remind that we still need to transform the whole dataset first
X_std = sc.transform(X)
plot_decision_regions(X_std, y, classifier=my_model)

from sklearn.linear_model import LogisticRegression

lr = LogisticRegression()
lr.fit(X_train_std, y_train)

X_std = sc.transform(X)
plot_decision_regions(X_std, y, classifier=lr)