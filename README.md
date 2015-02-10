# number2words

Java classes that can be used to convert numbers into words in different languages

## usage

```java
import com.mambu.number2words.api.NumberTranscriber;
import com.mambu.number2words.api.factories.NumberTranscriberFactory;

NumberTranscriber transcriber = NumberTranscriberFactory.newTranscriber(Locale.ENGLISH);

transcriber.toWords(new BigDecimal("1234"));    // returns "one thousand two hundred thirty four" 
transcriber.toWords(new BigDecimal("1234.12")); // returns "one thousand two hundred thirty four **and** twelve"

```

###Supported languages:

| Locale              | Language           | Notes                                               |
|---------------------|:------------------:|----------------------------------------------------:|
|Locale.ENGLISH       | English            | Small scale. Values over 1 trillion overflow .      |
|Locale.SIMPLIFIED_CHINESE| Simplified Chinese | Uses **financial numerals**. Values over 10^12 overflow.|
