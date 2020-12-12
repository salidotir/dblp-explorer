# dblp-explorer
Citation Network Analysis

This is a java program taht can find most relevant papaers to papers found by searching for a keyword in a large json file containing papares information.

## Requirements
- Use **Jackson* Streaming API* for reading the large json file.

- Use **Maven** for including Jackson.

- To include Jackson, add the below code to dependancies of *"pom.xml"*:
```xml
<dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-xc</artifactId>
    <version>1.9.12</version>
</dependency>
```

## Usage
The program gets the address of json file, a keyword and N as in the input.

It first searches the articles with titles containing the keyword, called seached_papers.

Then it finds papers' ids all tiers up to level N.

*Tier-1 papers are those which are cited by searched_papers.*

*Tier-2 papers are those which are cited by tier-1 papers.*

*Tier-k papers are those which are cited by tier-(k-1) papers.*

```java
citationNetworkAnalysis.process_find_keywork(jsonFilePath, keyword);            // returns searched_papers

citationNetworkAnalysis.process_find_tier_k(jsonFilePath, searched_papers);     // returns papers cited by searched_papers
```

## Sample output
The sample output of running program with file [*"sample.json"*](https://github.com/salidotir/dblp-explorer/blob/main/sample.json) & *keyword="for"* & *N=2* is uploaded as [*"out.txt"*](https://github.com/salidotir/dblp-explorer/blob/main/out.txt).
