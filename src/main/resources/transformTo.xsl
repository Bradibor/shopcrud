<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="@* | node()" name="identity">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/shops">
        <shops>
            <xsl:apply-templates/>
        </shops>
    </xsl:template>

    <xsl:template match="//shop">
        <shop id="{@id}">
            <xsl:apply-templates/>
        </shop>
    </xsl:template>

    <xsl:template match="//address">
        <address>
            <xsl:attribute name="city">
                <xsl:value-of select="city"/>
            </xsl:attribute>
            <xsl:attribute name="street">
                <xsl:value-of select="street"/>
            </xsl:attribute>
            <xsl:attribute name="house">
                <xsl:value-of select="house"/>
            </xsl:attribute>
            <xsl:attribute name="building">
                <xsl:value-of select="building"/>
            </xsl:attribute>
        </address>
    </xsl:template>

    <xsl:template match="//quantity">
        <xsl:if test="@type = 'container'">
            <container>
                <xsl:copy-of select="node()"/>
            </container>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>