package com.justacompany.userlocationtracking.datatransferobject

import com.vividsolutions.jts.geom.Coordinate

data class NormalizedCoordinates (
        val normalizedChunkedCoordinates: List<List<Coordinate>>,
        val residualCoordinates: List<Coordinate>
)