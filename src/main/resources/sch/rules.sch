<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
<title>Check Report</title>
  <pattern name="structure-check">
   <rule context="globalreport">
	<assert test="individualreports">Individual reports is mandatory</assert>
   </rule>
   <rule context="individualreports">
	<assert test="report">At least one report is required</assert>
   </rule>
   <rule context="report">
	<assert test="tiff_structure">Tiff structure is mandatory</assert>
   </rule>
   <rule context="tiff_structure">
	<assert test="ifdTree">Ifd tree is mandatory</assert>
   </rule>
   <rule context="ifdTree">
    <assert test="count(ifdNode)!= 0">The Tiff file must have at least one IFD</assert>
   </rule>
   <rule context="tags">
    <assert test="count(tag)!= 0">IFDs must have at least one tag</assert>
   </rule>
  </pattern>
  <pattern name="image-dimensions-check">
   <rule context="report">
	<assert test="width">Image width is mandatory</assert>
   </rule>
  </pattern>
</schema>