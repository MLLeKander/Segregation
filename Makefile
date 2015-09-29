.SUFFIXES: .java .class
.java.class:
	javac $*.java

FILES = \
		 Agent.java Board.java Point.java Arguments.java Colors.java \
		 AbstractAgent.java \
		 MigrationStrategy.java ClosestSatisfied.java CompositeStrategy.java MostSatisfied.java \
		 SchellingAgent.java DoubleAgent.java MultiAgent.java \
		 BoardFactory.java Main.java \
		 Metric.java AbstractMetric.java SimilarityMetric.java ClusteringMetric.java

all: Segregation.jar

classes: $(FILES:.java=.class)

Segregation.jar: classes
	jar cfe Segregation.jar Main *.class

clean:
	rm *.class
