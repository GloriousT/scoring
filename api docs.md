

Yahoo has gone to a Reactjs front end which means if you analyze the request headers from the client to the backend
 you can get the actual JSON they use to populate the client side stores.
Hosts:

    query1.finance.yahoo.com HTTP/1.0
    query2.finance.yahoo.com HTTP/1.1 (difference between HTTP/1.0 & HTTP/1.1)

If you plan to use a proxy or persistent connections use query2.finance.yahoo.com. But for the purposes of this post
 the host used for the example URLs is not meant to imply anything about the path it's being used with.
Fundamental Data

    /v10/finance/quoteSummary/AAPL?modules= (Full list of modules below)

(substitute your symbol for: AAPL)

Inputs for the ?modules= query:

    modules = [
       'assetProfile',
       'summaryProfile',
       'summaryDetail',
       'esgScores',
       'price',
       'incomeStatementHistory',
       'incomeStatementHistoryQuarterly',
       'balanceSheetHistory',
       'balanceSheetHistoryQuarterly',
       'cashflowStatementHistory',
       'cashflowStatementHistoryQuarterly',
       'defaultKeyStatistics',
       'financialData',
       'calendarEvents',
       'secFilings',
       'recommendationTrend',
       'upgradeDowngradeHistory',
       'institutionOwnership',
       'fundOwnership',
       'majorDirectHolders',
       'majorHoldersBreakdown',
       'insiderTransactions',
       'insiderHolders',
       'netSharePurchaseActivity',
       'earnings',
       'earningsHistory',
       'earningsTrend',
       'industryTrend',
       'indexTrend',
       'sectorTrend' ]

Example URL:

    https://query1.finance.yahoo.com/v10/finance/quoteSummary/AAPL?modules=assetProfile%2CearningsHistory

Querying for: assetProfile and earningsHistory

The %2C is the Hex representation of , and needs to be inserted between each module you request. details about the
 hex encoding bit(if you care)
Options contracts

    /v7/finance/options/AAPL (current expiration)
    /v7/finance/options/AAPL?date=1579219200 (January 17, 2020 expiration)

Example URL:

    https://query2.yahoo.finance.com/v7/finance/options/AAPL (current expiration)
    https://query2.yahoo.finance.com/v7/finance/options/AAPL?date=1579219200 (January 17, 2020 expiration)

Any valid future expiration represented as a UNIX timestamp can be used in the ?date= query. If you query for
 the current expiration the JSON response will contain a list of all the valid expirations that can be used in
  the ?date= query. (here is a post explaining converting human readable dates to unix timestamp in Python)
Price

    /v8/finance/chart/AAPL?symbol=AAPL&period1=0&period2=9999999999&interval=3mo

Intervals:

    &interval=3mo 3 months, going back until initial trading date.
    &interval=1d 1 day, going back until initial trading date.
    &interval=5m 5 minuets, going back 80(ish) days.
    &interval=1m 1 minuet, going back 4-5 days.

How far back you can go with each interval is a little confusing and seems inconsistent. My assumption is that
 internally yahoo is counting in trading days and my naive approach was not accounting for holidays. Although
 that's a guess and YMMV.

period1=: unix timestamp representation of the date you wish to start at. Values below the initial
 trading date will be rounded up to the initial trading date.

period2=: unix timestamp representation of the date you wish to end at. Values greater than the last
trading date will be rounded down to the most recent timestamp available.

Note: If you query with a period1= (start date) that is too far in the past for the interval you've
 chosen, yahoo will return prices in the 3mo interval regardless of what interval you requested.

Add pre & post market data

    &includePrePost=true

Add dividends & splits

    &events=div%2Csplit

Example URL:

    https://query1.finance.yahoo.com/v8/finance/chart/AAPL?symbol=AAPL&period1=0&period2=9999999999&interval=1d&includePrePost=true&events=div%2Csplit

The above request will return all price data for ticker AAPL on a 1 day interval including pre
 and post market data as well as dividends and splits.

Note: the values used in the price example url for period1= & period2= are to demonstrate the
 respective rounding behavior of each input.



It looks like they have started adding a required cookie, but you can retrieve this fairly easily, for example:

    GET https://uk.finance.yahoo.com/quote/AAPL/history

Responds with the header in the form:

    set-cookie:B=xxxxxxxx&b=3&s=qf; expires=Fri, 18-May-2018 00:00:00 GMT; path=/; domain=.yahoo.com

You should be able to read this and attach it to your .csv request:

    GET https://query1.finance.yahoo.com/v7/finance/download/AAPL?period1=1492524105&period2=1495116105&interval=1d&events=history&crumb=tO1hNZoUQeQ
cookie: B=xxxxxxxx&b=3&s=qf;

Note the crumb query parameter, this seems to correspond to your cookie in some way. Your best bet is to scrape this from the HTML response to your initial GET request. Within that response, you can do a regex search for: "CrumbStore":\{"crumb":"(?<crumb>[^"]+)"\} and extract the crumb matched group.

It looks like once you have that crumb value though you can use it with the same cookie on any symbol/ticker for the next year meaning you shouldn't have to do the scrape too frequently.

To get current quotes just load:

https://query1.finance.yahoo.com/v8/finance/chart/AAPL?interval=2m

With:

    AAPL substituted with your stock ticker
    interval one of [1m, 2m, 5m, 15m, 30m, 60m, 90m, 1h, 1d, 5d, 1wk, 1mo, 3mo]
    optional period1 query param with your epoch range start date e.g. period1=1510340760
    optional period2 query param with your epoch range end date e.g. period2=1510663712


