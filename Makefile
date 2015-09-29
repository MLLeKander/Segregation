.SUFFIXES: .java .class
.java.class:
	javac $*.java

FILES = \
		 Agent.java Board.java Point.java Arguments.java Colors.java \
		 AbstractAgent.java \
		 MigrationStrategy.java ClosestSatisfied.java CompositeStrategy.java MostSatisfied.java \
		 SchellingAgent.java \
		 DoubleAgent.java \
		 BoardFactory.java Main.java
 # DoubleBoard.java DoubleMain.java
 # SchellingBoard.java SchellingMain.java

all: Segregation.jar

classes: $(FILES:.java=.class)

Segregation.jar: classes
	jar cfe Segregation.jar Main *.class

clean:
	rm *.class
