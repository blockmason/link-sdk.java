.PHONY: \
	build \
	build-sources \
	build-tests \
	clean \
	default \
	install \
	package \
	test

default: install build test package

clean:
	rm -fR lib .toolchain/dependencies

build: build-sources build-tests

build-sources:
	find src/main/java -type f -name '*.java' | xargs javac --class-path="$(shell find .toolchain/dependencies -type f | xargs | tr ' ' ':')" -encoding UTF-8 -d lib

build-tests: build-sources
	find src/test/java -type f -name '*.java' | xargs javac --class-path="$(shell find .toolchain/dependencies -type f | xargs | tr ' ' ':'):lib" -encoding UTF-8 -d lib

install:
	.toolchain/dependency org.json json 20180813 jar
	.toolchain/dependency org.junit.jupiter junit-jupiter-api 5.4.2 jar
	.toolchain/dependency org.junit.platform junit-platform-console-standalone 1.4.2 jar

package:
	(cd lib; find * -type f -name '*.class' | grep -v 'Test\.class' | xargs jar --create --main-class=io.blockmason.link.Main --file=../.toolchain/dependencies/io.blockmason.link-1.0.0-SNAPSHOT.jar)

test: build
	java -jar .toolchain/dependencies/org.junit.platform.junit-platform-console-standalone-1.4.2.jar --include-package=io.blockmason.link --class-path="$(shell find .toolchain/dependencies -type f | xargs | tr ' ' ':'):lib" --scan-classpath=lib
