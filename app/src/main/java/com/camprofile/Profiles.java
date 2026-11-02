package com.camprofile;

public class Profiles {
    static final String PIX = "HDR+ 1.0.123456789z";
    // {watermark, make, exifModel, software}  -- Software must be <= 27 chars
    public static final String[][] ALL = {
        // Google Pixel (index 2 = default)
        {"Pixel 10 Pro XL", "Google", "Pixel 10 Pro XL", PIX},
        {"Pixel 10 Pro",    "Google", "Pixel 10 Pro",    PIX},
        {"Pixel 10",        "Google", "Pixel 10",        PIX},
        {"Pixel 9 Pro XL",  "Google", "Pixel 9 Pro XL",  PIX},
        {"Pixel 9 Pro Fold","Google", "Pixel 9 Pro Fold",PIX},
        {"Pixel 9",         "Google", "Pixel 9",         PIX},
        {"Pixel 9a",        "Google", "Pixel 9a",        PIX},
        // Samsung
        {"Galaxy S25 Ultra","samsung","SM-S938B","S938BXXU1AYA1"},
        {"Galaxy S25+",     "samsung","SM-S936B","S936BXXU1AYA1"},
        {"Galaxy S25",      "samsung","SM-S931B","S931BXXU1AYA1"},
        {"Galaxy S25 Edge", "samsung","SM-S937B","S937BXXU1AYA1"},
        {"Galaxy Z Fold7",  "samsung","SM-F966B","F966BXXU1AYA1"},
        {"Galaxy Z Flip7",  "samsung","SM-F766B","F766BXXU1AYA1"},
        {"Galaxy S24 Ultra","samsung","SM-S928B","S928BXXU1AXA1"},
        {"Galaxy A56",      "samsung","SM-A566B","A566BXXU1AYA1"},
        // Apple
        {"iPhone 17 Pro Max","Apple","iPhone 17 Pro Max","26.0.1"},
        {"iPhone 17 Pro",    "Apple","iPhone 17 Pro",    "26.0.1"},
        {"iPhone 17",        "Apple","iPhone 17",        "26.0.1"},
        {"iPhone Air",       "Apple","iPhone Air",       "26.0.1"},
        {"iPhone 16 Pro Max","Apple","iPhone 16 Pro Max","18.6"},
        {"iPhone 16 Pro",    "Apple","iPhone 16 Pro",    "18.6"},
        {"iPhone 16",        "Apple","iPhone 16",        "18.6"},
        // Nothing
        {"Nothing Phone (3)", "Nothing","A024","Nothing OS 4.0"},
        {"Nothing Phone (3a)","Nothing","A059","Nothing OS 3.2"},
        {"Nothing Phone (2)", "Nothing","A065","Nothing OS 3.0"},
        // Others
        {"OnePlus 13",       "OnePlus","CPH2655",  "OnePlus Camera"},
        {"Xiaomi 15 Ultra",  "Xiaomi", "25010PN30C","Xiaomi Camera"},
        {"OPPO Find X8 Pro", "OPPO",   "CPH2659",  "OPPO Camera"},
        {"vivo X200 Pro",    "vivo",   "V2405",    "vivo Camera"},
        {"Xperia 1 VI",      "Sony",   "XQ-EC72",  "Sony Camera"}
    };
}
