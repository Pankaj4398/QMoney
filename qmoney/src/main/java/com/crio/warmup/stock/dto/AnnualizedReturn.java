
package com.crio.warmup.stock.dto;

import java.util.Comparator;

public class AnnualizedReturn {

  private final String symbol;
  private final Double annualizedReturn;
  private final Double totalReturns;

  public AnnualizedReturn(String symbol, Double annualizedReturn, Double totalReturns) {
    this.symbol = symbol;
    this.annualizedReturn = annualizedReturn;
    this.totalReturns = totalReturns;
  }

  public String getSymbol() {
    return symbol;
  }

  public Double getAnnualizedReturn() {
    return annualizedReturn;
  }

  public Double getTotalReturns() {
    return totalReturns;
  }

  public static final Comparator<AnnualizedReturn> totalReturnComparator = new 
      Comparator<AnnualizedReturn>() {         
    @Override         
    public int compare(AnnualizedReturn jc1, AnnualizedReturn jc2) {             
      return (jc2.getTotalReturns() < jc1.getTotalReturns() ? -1 :                     
              (jc2.getTotalReturns().equals(jc1.getTotalReturns()) ? 0 : 1));           
    }     
  };
}
