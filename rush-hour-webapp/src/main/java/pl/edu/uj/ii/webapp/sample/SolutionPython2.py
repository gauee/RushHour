testCases = int(raw_input())
for i in xrange(0, testCases):
    carsAmount = int(raw_input())
    for j in xrange(0, carsAmount):
        raw_input()

steps = ["C R 1", "N R 2", "M R 2", "X R 4"]
for step in steps:
    print step
