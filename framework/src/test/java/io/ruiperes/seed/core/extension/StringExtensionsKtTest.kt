package io.ruiperes.seed.core.extension

import java.math.BigDecimal

class StringExtensionsKtTest {
    @Test
    fun `decimal test When string has dot Should return Decimal`() {
        // Arrange
        val numberString = "2,105.88"

        // Act
        val result = numberString.parseToBigDecimal()

        // Assert
        val numberStringFixed = "2105.88"
        assertEquals(BigDecimal(numberStringFixed), result)
    }

    @Test
    fun `decimal test When string has comma Should return Decimal`() {
        // Arrange
        val numberString = "2.105,88"

        // Act
        val result = numberString.parseToBigDecimal()

        // Assert
        val numberStringFixed = "2105.88"
        assertEquals(BigDecimal(numberStringFixed), result)
    }

    @Test
    fun `decimal test When string has multiple comma Should return Decimal`() {
        // Arrange
        val numberString = "2,105,000.88"


        // Act
        val result = numberString.parseToBigDecimal()

        // Assert
        val numberStringFixed = "2105000.88"
        assertEquals(BigDecimal(numberStringFixed), result)
    }

    @Test
    fun `decimal test When string has multiple dot Should return Decimal`() {
        // Arrange
        val numberString = "2.105.000,88"

        // Act
        val result = numberString.parseToBigDecimal()

        // Assert
        val numberStringFixed = "2105000.88"
        assertEquals(BigDecimal(numberStringFixed), result)
    }

    @Test
    fun `decimal test When string has multiple dot without decimal Should return Decimal`() {
        // Arrange
        val numberString = "2.105.000.888"

        // Act
        val result = numberString.parseToBigDecimal()

        // Assert
        val numberStringFixed = "2105000888"
        assertEquals(BigDecimal(numberStringFixed), result)
    }

    @Test
    fun `decimal test When string has multiple comma without decimal Should return Decimal`() {
        // Arrange
        val numberString = "2,105,000,888"

        // Act
        val result = numberString.parseToBigDecimal()

        // Assert
        val numberStringFixed = "2105000888"
        assertEquals(BigDecimal(numberStringFixed), result)
    }

    @Test
    fun `decimal test When string has multiple comma and dot without decimal Should return Decimal`() {
        // Arrange
        val numberString = "2,105,000,888.000.000"

        // Act
        val result = numberString.parseToBigDecimal()

        // Assert
        val numberStringFixed = "2105000888000000"
        assertEquals(BigDecimal(numberStringFixed), result)
    }

    @Test
    fun `decimal test When string has multiple comma and dot Should return Decimal`() {
        // Arrange
        val numberString = "2.105.000,888.000.000"

        // Act
        val result = numberString.parseToBigDecimal()

        // Assert
        val numberStringFixed = "2105000.888000000"
        assertEquals(BigDecimal(numberStringFixed), result)
    }

    @Test
    fun `decimal test When string has multiple fraction digits Should return Decimal`() {
        // Arrange
        val numberString = "2.123456"

        // Act
        val result = numberString.parseToBigDecimal()

        // Assert
        val numberStringFixed = "2.123456"
        assertEquals(BigDecimal(numberStringFixed), result)
    }
}
