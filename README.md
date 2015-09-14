# Segregation Modelling
Project for Distributed Multi-Agent Systems, an extension of Thomas Schelling's model proposed in "Dynamic Models of Segregation" (1971).

## Build
`javac *.java`

## Run
`javac SchellingMain`

Optional runtime parameters are:

- **--seed** (long, default 1): RNG seed for initial agent placement
- **--maxIters** (int, default 100): Maximum number of epochs simulated before terminating early
- **--rows** (int, default 13): Number of rows in the 2-d world
- **--cols** (int, default 16): Number of columns in the 2-d world
- **--similarity** (double, default 0.4): Percentage of neighbors that must be of the same color for an agent to be satisfied
- **--wThresh** (double, default 0.4): A value between 0 and 1 that gives the probability of an agent being white
- **--bThresh** (double, default 0.4): A value between 0 and 1 that gives the probability of an agent being black
