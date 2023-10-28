package TP2.results;

public interface Result {
    static final String[] ASSERTIONS = {"assertTrue", "assertFalse", 
                                                "assertEquals", "assertNotEquals",
                                                "assertSame", "assertNotSame",
                                                "assertNull", "assertNotNull",
                                                "assertArrayEquals", "assertThrows",
                                            };

    public String getCsvHeader();
    
    public String toCsv();
}
