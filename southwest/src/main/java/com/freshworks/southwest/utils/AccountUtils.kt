package com.freshworks.southwest.utils

import com.freshworks.sdk.data.SDKConfig

object AccountUtils {

    val configEU = SDKConfig(
        appId = "5c55a09f-7058-4b3f-92e2-2a2491508dc0",
        appKey = "233a63f9-648d-4463-a0ac-a84ae8022b16",
        domain = "msdk.eu.freshchat.com",
        widgetUrl = "//eu.fw-cdn.com/10429467/299293.js"
    )

    val configAU = SDKConfig(
        appId = "abeaa0e6-b2de-4852-9a75-90d91e64294f",
        appKey = "eb7a2fa0-edeb-468e-bbcd-3459103f30c0",
        domain = "msdk.au.freshchat.com",
        widgetUrl = "//au.fw-cdn.com/20174488/94019.js"
    )

    val branch = SDKConfig(
        appId = "ad762806-1c9b-449f-b0d7-d8d06376ba6c",
        appKey = "3a0f6555-c5ac-4475-9100-10ddebd8187f",
        domain = "rich-media-testing.freshpori.com",
        widgetUrl = "???"
    )

    val staging = SDKConfig(
        appId = "accf0b5a-2034-43e1-875a-de9c5e4f3955",
        appKey = "3a7a632a-32f0-44db-93b2-113c8867817e",
        domain = "mobiletestingstaging-org-844f4ac14e9078016883932.freshpori.com",
        widgetUrl = "//fm-staging-us-app-cdnjs.s3.amazonaws.com/crm/7575962/6419591.js"
    )

    val jwtEnabled = SDKConfig(
        appId = "63ba351b-44ef-4a19-a1e6-86c33de29965",
        appKey = "02c3bebd-86e5-4603-b25c-864a3049942a",
        domain = "msdk.eu.freshchat.com",
        widgetUrl = "//eu.fw-cdn.com/10350592/249750.js",
        jwtAuthToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.e30.I7ALrKji1e99LOkulaGH4BVcly" +
            "BuWyOQLpNGuXJ5jPfspznjp7P_7Uh51uPhlg5cLcb52uUZTa5zc35v04m95JlpAbiskdZO2NWxdGnm" +
            "JOJPdzxXtI3tr24fCCYnpKzABieUSGWa2HnPm_wM27HHTGTNCvuLcfseqegsejII6MDpL_Rv-" +
            "NIlqk-Xm2sNHOKRNQcKcQn0QDRBCJB75gJ-5TK6MFzo-vDPBZD-CtgHa8jYbj7dP30joa7ubzq" +
            "DjuXnVDUWAIDefCAr9biEshwJ9EOq0zKObYTdFECEr7k3H2WPCvNh1GGum2a0D0wPTstnR5dSs" +
            "smJXCuxrWMGqWeP3Q",
        // Uncomment the below token for valid Auth token
        /*     jwtAuthToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdF9uYW1lIjoiSm9obiIsImxhc" +
                 "3RfbmFtZSI6IkRvZSIsImVtYWlsIjoiam9obkBkb2UuY29tIiwicmVmZXJlbmNlX2lkIjoiYWJjMTIzIiwiZnJ" +
                "lc2hjaGF0X3V1aWQiOiJiZTdiZTNkMS02NTkwLTQ1NTgtYmRhMi1hYzYyOGNhZDA1MDYifQ.hPVxXKaISJqSWv2" +
                 "uGIyePf2xGbk3Y8a15XSO3b_poMwbPvGtSdS78cn8GP9GyUO6PJdzjoR7uCpIpDPEaFdB0KiP8pyi4G9wXf53oeEG" +
                 "sx8gUD3odTlCOB2gtYQfs-7BE8-vkPfiAHPG2lL_lz-7An1SSZ-dt4pCQ-qYa0LnJU3Gn-kOWPwZjz8Z-Z1uanAHe17" +
                 "Jz0O3Om9Zw_OCKr0ZUr2DxlpKwY5UDocusz9rI_vyKfs-vqZj8JjTmdH2KOgjPFx78dIym0woHpGM7T4h8tx2rLFhqDC" +
                 "3vhalqJT_T3kEE9NVIatdbz-M-xNN5ktmXJv3aXRyWks6BzWL8l9lfQ"*/
    )
}