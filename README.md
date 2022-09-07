# Extra functions for Lucene English analyzers

This package contains some functions that can be used to tweak an English analyzer, some general and some specific to BDRC.

### UK - US spelling variants

UKUSSpellingVariants class has a `smap` field which is a SynonymMap that can be used in a `SynonymGraphFilter` to normalize spelling variations between different English languages, mostly UK and US.

The list has been compiled based on:
- http://www.tysto.com/uk-us-spelling-list.html
- https://wiki.ubuntu.com/EnglishTranslation/WordSubstitution
- https://en.wikipedia.org/wiki/Wikipedia:List_of_spelling_variants

### BDRC English Analyzer

This is an analyzer similar to the Lucene English Analyzer, with the following changes:
- a `SynonymGraphFilter` with the UK - US spelling variants
- an `ASCIIFoldingFilter`
- a `MappingCharFilter` to simplify patterns commonly found in transliteration of Asian languages (ex: `ś` -> `sh`)
