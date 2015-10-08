# Segregation Modelling
Project for Distributed Multi-Agent Systems, an extension of Thomas Schelling's model proposed in "Dynamic Models of Segregation" (1971).

## Build
`javac *.java`, or just `make`.

## Run
`java Main`, or via the jar: `java -jar Segregation.jar`

## Simulation settings

Optional runtime parameters for `Main` are:

- `--agentType` (string, default "double"): Type of agent to use. Valid agent types are "shelling", "double", and "multi". See below for agent-specific documentation.
- `--seed` (long, default 1): RNG seed for initial agent creation and placement.
- `--rows` (int, default 13): Number of rows in the 2-d world.
- `--cols` (int, default 16): Number of columns in the 2-d world.
- `--similarity` (double, default 0.4): Minimum percentage of neighbors that must be of the same color for an agent to be satisfied.
- `--similarityMax` (double, default 1): Maximum percentage of neighbors that may be of the same color for an agent to be satisfied.
- `--empty` (double, default 0.2): A value between 0 and 1 that gives the probability of a space being empty.
- `--maximizer` (boolean, default false): Agent will use a maximizer movement strategy if true, and a local satisficer strategy otherwise.

- `--maxIters` (int, default 100): Maximum number of epochs simulated before terminating early.
- `--animate` (boolean, default false): If true, the program prints intermediate states.
- `--sleep` (long, default 0): If greater than 0 and `--animate` is true, sleep for the specified number of milliseconds after each animation step.
- `--color` (boolean, default true): If true, the program prints in a colorized format.
- `--printAgents` (boolean, default true): If true, the program prints a text representation of the agents.
- `--printSimilarity` (boolean, default true): If true, the program prints the neighborhood similarity percentages of each agent.
- `--metrics` (boolean, default true): If true, the program prints a set of metrics about the run.
- `--headless` (boolean, default false): If true, the program does not print any board state.
- `--batch` (boolean, default false): If true, the program does a batch run (see below). Sets `--metrics` and `--headless` to true, if not already specified.

### Schelling Agent

An agent with one feature. Optional parameters are:

- `--thresh` (double, default 0.5): A value between 0 and 1 that gives the probability of an agent being Black.

### Double Agent

An agent with two features. Optional parameters are:

- `--aThresh` (double, default 0.5): A value between 0 and 1 that gives the probability of an agent having Feature A.
- `--bThresh` (double, default 0.5): A value between 0 and 1 that gives the probability of an agent having Feature B.

### Multi Agent

An agent with an arbitrary number of features. Optional parameters are:

- `--numFeatures` (int, default 5): The number of features an agent has.
- `--thresh` (double, default 0.5): A value between 0 and 1 that gives the probability of an agent having each feature. Sets each feature at an identical weight.
- `--threshes` (string): A colon-seperated (:) list of threshold values between 0 and 1. If set, this overrides `--numFeatures` and `--thresh`.

## Batch Runs

A batch run can be done to perform a simulation across multiple random initial board states, writing resulting metrics to a specified file.

Batch-specific options are as follows:

- `--batch` (boolean, default false): If true, the program does a batch run. Sets `--metrics` and `--headless` to true, if not already specified.
- `--batchMin` (long, default 0): Minimum RNG seed to iterate over, inclusive.
- `--batchMax` (long, required): Maximum RNG seed to iterate over, exclusive.
- `--outFile` (string, required): Path to output file that will contain metrics.

The output written to `--outFile` is a tab-separated file with a header row containing column names.
Each row in this file corresponds to a single simulation run.

In order, each row of the output file contains:

- `numFeatures`: The number of features the agent have.
- `seed`: The RNG seed used.
- `similarity`: The minimum similarity preference the agents have.
- `emptiness`: The fraction of cells left empty.
- `rows`: Number of rows in the 2-d world.
- `cols`: Number of columns in the 2-d world.
- `strat`: The migration strategy the agents use, either "maximizer" or "satisficer".
- `threshes`: A comma-seperated list of thresholds used to generate these agents.
- `numEpochs`: The number of epochs required before the simulation ended.
- `beforeSimilarity`: A comma-seperated list of neighborhood similarity percentages for each agent in the initial state, sorted.
- `beforeMooreClustering`: A comma-seperated list of the size of each Moore cluster in the initial state, sorted.
- `beforeNeumannClustering`: A comma-seperated list of the size of each von Neumann cluster in the initial state, sorted.
- `afterSimilarity`: A comma-seperated list of neighborhood similarity percentages for each agent in the final state, sorted.
- `afterMooreClustering`: A comma-seperated list of the size of each Moore cluster in the final state, sorted.
- `afterNeumannClustering`: A comma-seperated list of the size of each von Neumann cluster in the final state, sorted.
