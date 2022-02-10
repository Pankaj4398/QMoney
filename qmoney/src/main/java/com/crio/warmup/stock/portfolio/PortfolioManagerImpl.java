
package com.crio.warmup.stock.portfolio;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {

  private RestTemplate restTemplate;

  // Caution: Do not delete or modify the constructor, or else your build will
  // break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  // TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from
  // main anymore.
  // Copy your code from Module#3
  // PortfolioManagerApplication#calculateAnnualizedReturn
  // into #calculateAnnualizedReturn function here and ensure it follows the
  // method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required
  // further as our
  // clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command
  // below:
  // ./gradlew test --tests PortfolioManagerTest

  // CHECKSTYLE:OFF
  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades, 
      LocalDate endDate) {

    AnnualizedReturn annualizedReturn;

    List<AnnualizedReturn> annualizedReturns = new ArrayList<AnnualizedReturn>();

    for (int i = 0; i < portfolioTrades.size(); i++) {
      annualizedReturn = getAnnualizedReturn(portfolioTrades.get(i), endDate);

      annualizedReturns.add(annualizedReturn);
    }

    Collections.sort(annualizedReturns, getComparator());
    return annualizedReturns;

    // return null;
  }

  private AnnualizedReturn getAnnualizedReturn(PortfolioTrade trade, LocalDate endLocalDate) {
    AnnualizedReturn annualizedReturn;
    String symbol = trade.getSymbol();
    LocalDate startLocalDate = trade.getPurchaseDate();

    try {
      // Fetch Data
      List<Candle> stocks;
      stocks = getStockQuote(symbol, startLocalDate, endLocalDate);
      // Extraxt stocks for startDate & endDate
      Candle stockStartDate = stocks.get(0);
      Candle stockLatest = stocks.get(stocks.size() - 1);

      Double buyPrice = stockStartDate.getOpen();
      Double sellPrice = stockLatest.getClose();
      // calculate total and annual returns
      Double totalReturns = (sellPrice - buyPrice) / buyPrice;

      Double numsYear = (double) ChronoUnit.DAYS.between(startLocalDate, endLocalDate) / 365;

      Double annualReturn = Math.pow(1 + totalReturns, (1 / numsYear)) - 1;

      annualizedReturn = new AnnualizedReturn(symbol, annualReturn, totalReturns);

    } catch (JsonProcessingException e) {
      annualizedReturn = new AnnualizedReturn(symbol, Double.NaN, Double.NaN);
    }

    return annualizedReturn;
  }

  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  // CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  // Extract the logic to call Tiingo third-party APIs to a separate function.
  // Remember to fill out the buildUri function and use that.

  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) 
      throws JsonProcessingException {

    if (from.compareTo(to) >= 0) {
      throw new RuntimeException();
    }
    String url = buildUri(symbol, from, to);
    String response = restTemplate.getForObject(url, String.class);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    Candle[] candlesArr = mapper.readValue(response, TiingoCandle[].class);

    List<Candle> stockList = new ArrayList<>();

    if (candlesArr != null) {
      // for (Candle cd : candlesArr) {
      // listCandles.add(cd);
      // }
      stockList = Arrays.asList(candlesArr);
    }

    return stockList;

    // return null;
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String uriTemplate = "https://api.tiingo.com/tiingo/daily/$SYMBOL/prices?"
        + "startDate=$STARTDATE&endDate=$ENDDATE&token=$APIKEY";

    String token = "9dab23eb5ff0d7ac699ce297f44a62b1bac1ad14";

    String url = uriTemplate.replace("$SYMBOL", symbol).replace("$STARTDATE", startDate.toString())
        .replace("$ENDDATE", endDate.toString()).replace("$APIKEY", token);

    return url;
  }

}
