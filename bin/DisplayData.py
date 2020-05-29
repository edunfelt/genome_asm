import matplotlib.pyplot as plt
from collections import Counter
import numpy as np

file = input()
with open(file) as f:
    contigs = f.readline()
    contigSizes = f.readline().split()

    components = f.readline()
    compSizes = f.readline().split()
    lines = f.readlines()

a = Counter(contigSizes)
b = Counter(compSizes)

labels_a = list(a.keys())
values_a = list(a.values())
labels_b = list(b.keys())
values_b = list(b.values())

y_pos_a = np.arange(len(labels_a))
y_pos_b = np.arange(len(labels_b))

fig, axes = plt.subplots(nrows=2, ncols=1)

plt.subplot(2, 1, 1)
plt.bar(y_pos_a, values_a, align='center', alpha=0.5)
plt.xticks(y_pos_a, labels_a)
plt.ylabel('Degree')
plt.title('Contig degrees, ' + contigs + "number of contigs")

plt.subplot(2, 1, 2)
plt.bar(y_pos_b, values_b, align='center', alpha=0.5)
plt.xticks(y_pos_b, labels_b)
plt.ylabel('Sizes')
plt.title('Component sizes, ' + components + "number of components")

fig.tight_layout()
fig.savefig('results_' + file + '.png')
plt.show()
