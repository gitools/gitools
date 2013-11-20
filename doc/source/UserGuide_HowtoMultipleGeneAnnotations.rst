================================================================
How to add multiple gene annotations to heat maps
================================================================



This how-to explains how to retrieve and apply alternative gene annotations for a heat map that is labeled with Ensembl Gene IDs. Here, we are considering heat maps of either Intogen data or external user-specific data, both of human and non-human origin.

Start by downloading a tabular annotation file that contains the Ensembl ID (standard annotation for all Intogen data) in the first column and additional gene annotations in the following columns. You can do this using the Biomart connector in Gitools as follows:

1. Click on the Biomart icon in the main menu of Gitools, and select “data” to import.

2. In the next tab, as a Portal, select Ensembl 56 (which is the version used in Intogen currently). As Database, select Ensembl 56, and as Dataset, select Homo sapiens genes. When working with external data that have annotations from a different Ensembl version, choose this version instead.

3. In the next tab, you can select two or more attributes=alternative identifiers (each of them will be a column in the data table): click on “add” and select the attributes one by one from the long list. The first one should always be Ensembl Gene ID; the second one could be Gene>Ensembl>Associated gene name. On occasions, you might be interested in adding transcript IDs, protein IDs or orthologs from other species such as the Mouse Gene Ensembl IDs as additional columns.

4. Next is a “filters” tab, which you can skip. (explain??)

5. In the last tab, you are prompted a name and location to save the annotations file. Please note that these annotation tables have headers = column names, as opposed to module tables that have no header and ALWAYS 2 columns. Now use this file to annotate your heat map of Ensembl Gene IDs with the alternative identifier(s) you downloaded:

6. Open your heat map in Gitools.

7. In the “properties” tab, go to “rows”, click on “open annotations” and select the annotations file saved before.

8. Under “Labels”, click on “...” to see the list of headers from the annotations file. Select one annotation and click OK. You will see all selected annotations in the field “pattern”. Press enter to confirm and display the annotations next to the heat map.

9. You can display more than one annotation at once, for instance, ENS human - Gene ID - ENS mouse. Just open and select from “open annotations” more than once, or type in the headers directly to get something like: ${ annotation1}  ${ annotation2}  ${ annotation3}  in the field “pattern”. Hint: Use spaces between the annotations to see them more clearly in the heat map.

Hints related to this application that might prove useful for you:

- You can edit the annotations file manually using a spread sheet editor or a text editor. Be sure to stay always in tab-delimited format.

- Multiple annotations for columns work just as described for rows. Adjust the height of the annotations field if necessary in order to display the whole string of annotations on top of the heat map.

- CAUTION: Some gene annotations are not unique, such as the Gene>Ensembl>Associated gene name, meaning that e.g. a number of different Ensembl Gene IDs correspond to the same associated gene name. Trying to map these non-unique annotations to unique annotations will result in errors and should be avoided.
