# dblp-explorer design
Citation Network Analysis design

This is a java program that takes a large json file containing information on papers as input and search for relevant papers' ids.


## Reading json file
A small sample of the large json file is uploaded next to program files, *"sample.json"*.

As we are looking for papers' citations and reference, we do not need all informations included in th json file. The informations read for each document:
1. document id
2. document title
3. document authors
    - name
    - id
    - organization
4. document number of citations
5. document references


To specify the end of each object(document), we use the flag *"temp"* made *"true"* at reading the *"fos"* token, since it is the last token written in each object.


To specify the end of json file, add the following to the end of file:
```json
{
  "EOF": "true"
}
```

## Search for a keyword in paper title
Read the whole json file like the way explained earlier onece and at the time, check if document's tile contains the keyword and adds it to a list which will be returned:
```java
List<String> process_find_keywork(String jsonFilePath, String keyword);               // function definition
```

## Search for relevant papers all tiers up to level N
Read the whole json file for N times.

To find tier-k papers, read the whole json file like the way explained earlier and at the time, checks if any of tier-(k-1) papers are in document's references and adds it to a list which will be returned:

*"list_of_ids" will be updated in each loop.*
```java
List<String> process_find_tier_k(String jsonFilePath, List<String> list_of_ids);      // function definition
```
