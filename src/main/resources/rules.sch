<?xml version="1.0" encoding="utf-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron">
  <sch:pattern>
    <sch:title>Width</sch:title>
   <sch:rule context="report">
   <sch:assert test="check_value:width">
    Image width is mandatory.
   </sch:assert>
   <sch:assert test="check_value:height">
    Image height is mandatory.
   </sch:assert>
   </sch:rule>
  </sch:pattern>
</sch:schema>