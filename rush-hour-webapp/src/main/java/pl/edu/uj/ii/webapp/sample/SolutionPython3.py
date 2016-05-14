testCases = int(input())
for i in range(0, testCases):
    carsAmount = int(input())
    for j in range(0, carsAmount):
        input()

steps = [
    "0",
    "13",
    "I U 2",
    "E R 2",
    "C R 1",
    "M D 3",
    "X R 2",
    "A U 4",
    "X L 2",
    "M U 3",
    "E L 3",
    "C L 3",
    "M D 3",
    "I D 3",
    "X R 4"
]

for step in steps:
    print(step)
