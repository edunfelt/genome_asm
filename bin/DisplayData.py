import matplotlib.pyplot as plt
from collections import Counter

file = input()

# read file information
with open(file) as f:
    contigs = f.readline()
    line = f.readline()
    contigSizes = [int(item) for item in line.split()]

    components = f.readline()
    line = f.readline()
    compSizes = [int(item) for item in line.split()]

# contig degrees
degrees = sorted(Counter(contigSizes).most_common())
a = {k: v for (k, v) in degrees if k <= 20}
labels_a = list(a.keys())
values_a = list(a.values())

# component sizes
sizes = sorted(Counter(compSizes).most_common())
b = {k: v for (k, v) in sizes if k <= 20}
labels_b = list(b.keys())
values_b = list(b.values())

max_contig = str(degrees[-1][0])
max_comp = str(sizes[-1][0])

deg_sum = 0
for k, e in degrees:
    deg_sum += e*k
deg_mean = round(deg_sum / int(contigs))

size_sum = 0
for k, e in sizes:
    size_sum += e*k
size_mean = round(size_sum / int(components))


fig, axes = plt.subplots(nrows=2, ncols=1)

plt.subplot(2, 1, 1)
plt.bar(labels_a, values_a, align='center', alpha=0.5)
plt.xticks(labels_a)
plt.xlabel("Degree")
plt.ylabel('Occurrences')
plt.title('Contig degrees, ' + contigs + "number of contigs")

plt.subplot(2, 1, 2)
plt.bar(labels_b, values_b, align='center', alpha=0.5)
plt.xticks(labels_b)
plt.xlabel("Sizes")
plt.ylabel('Occurrences')
plt.title('Component sizes, ' + components + "number of components")

plt.figtext(.02, .02, "The maximum degree is " + max_contig + ", with mean " +
            str(deg_mean) + ", and the max " +
            "component size is " + max_comp + ",\n with mean " +
            str(size_mean) + ".")

fig.tight_layout()
fig.savefig('results_' + file + '.png')
plt.show()
