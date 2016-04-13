<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
<title>Check Report</title>
  <pattern>
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
  <pattern>
   <rule context="report">
	<assert test="ImageWidth">Image width is mandatory</assert>
	<assert test="PixelDensity">Pixel density is mandatory</assert>
   </rule>
   <rule context="pixeldensity">
     <assert test="@PixelDensity > 50">
      There pixel density is low.
      </assert>
  </rule>
  </pattern>
</schema>