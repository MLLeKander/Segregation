.SUFFIXES: .java .class
.java.class:
	javac $*.java

FILES = \
		 Agent.java Board.java Point.java Arguments.java Colors.java \
		 AbstractAgent.java \
		 MigrationStrategy.java ClosestSatisfied.java CompositeStrategy.java MostSatisfied.java \
		 SchellingAgent.java SchellingBoard.java SchellingMain.java \
		 DoubleAgent.java DoubleBoard.java DoubleMain.java

all: Segregation.jar

classes: $(FILES:.java=.class)

Segregation.jar: classes
	jar cfe Segregation.jar SchellingMain *.class

clean:
	rm *.class
