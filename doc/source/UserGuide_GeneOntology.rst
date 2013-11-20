================================================================
GeneOntology
================================================================

The Gene Ontology project is a major bioinformatics initiative with the aim of standardizing the representation of gene and gene product attributes across species and databases. The project provides a controlled vocabulary of terms for describing gene product characteristics. More information about Gene Ontology at  `http://www.geneontology.org/  <http://www.geneontology.org/>`__ .

In Gitools we allow to retrieve information about Gene Ontology terms for any type of identifiers available in  `Ensembl <http://www.ensembl.org>`__  database. So you can download Gene Ontology Biological Processes, Molecular Function or Cellular Location modules for gene symbols or for some affymetrix platform probe sets as far as the translation information is available in Ensembl. You can also retrieve this information for any organism available in Ensembl database.

Select category and version
-------------------------------------------------

In the first step you select the modules to download (biological processes, molecular function or cellular location) and the version of Ensembl to use for the translation of identifiers.

Select organism
-------------------------------------------------

The wizard will retrieve all the organisms available from Ensembl database and they will be shown in a selection box.

The user has to select one before continuing. There is a filter box that allow to reduce the long list of organisms by using keywords.

Select identifiers
-------------------------------------------------

The category of identifiers for the genes or other elements of your interest can be selected in this step. Only the identifiers available for the selected organism in Ensembl database will be shown.

Select destination
-------------------------------------------------

In this page you can select the name prefix for the files generated, the folder where they will be created and the file format.


Downloading the modules
-------------------------------------------------

Once all the parameters are selected and the user clicks on *Finish* button it starts downloading data from different sources in several steps depending on which module category, organism, and feature identifiers have been selected.

It will generate two files, one with the modules information and other with the annotations of the modules. The modules information contains lists of genes or features of interest that are related to the gene ontology terms, and the annotations file contains the textual description of each one.
