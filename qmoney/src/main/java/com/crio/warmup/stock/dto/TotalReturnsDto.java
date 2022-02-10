
package com.crio.warmup.stock.dto;

public class TotalReturnsDto implements Comparable<TotalReturnsDto> {
  
  private String symbol;
  private Double closingPrice;

  public TotalReturnsDto(String symbol, Double closingPrice) {
    this.symbol = symbol;
    this.closingPrice = closingPrice;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Double getClosingPrice() {
    return closingPrice;
  }

  public void setClosingPrice(Double closingPrice) {
    this.closingPrice = closingPrice;
  }

  @Override
  public int compareTo(TotalReturnsDto arg0) {
    return (this.getClosingPrice() < arg0.getClosingPrice() ? -1 : 
            (this.getClosingPrice().equals(arg0.getClosingPrice()) ? 0 : 1));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((closingPrice == null) ? 0 : closingPrice.hashCode());
    result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TotalReturnsDto other = (TotalReturnsDto) obj;
    if (closingPrice == null) {
      if (other.closingPrice != null) {
        return false;
      }
    } else if (!closingPrice.equals(other.closingPrice)) {
      return false;
    }
    if (symbol == null) {
      if (other.symbol != null) {
        return false;
      }
    } else if (!symbol.equals(other.symbol)) {
      return false;
    }

    return true;
  }
}
