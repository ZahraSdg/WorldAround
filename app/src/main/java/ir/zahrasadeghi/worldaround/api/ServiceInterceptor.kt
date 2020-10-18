package ir.zahrasadeghi.worldaround.api

import okhttp3.Interceptor
import okhttp3.Response


class ServiceInterceptor : Interceptor {

    companion object {

        const val CLIENT_ID = "GPHIVM4AC0ON5TW4HJE4TRD30VEPH01YV2W0KVF3YRMR321W"
        const val CLIENT_SECRET = "R1HDUATTSRMNOYEEKLFPSEOCOMTF0JEXOL2QU5XJTBEKEKRS"
        const val API_VER = "20200101"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("client_id", CLIENT_ID)
            .addQueryParameter("client_secret", CLIENT_SECRET)
            .addQueryParameter("v", API_VER)
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}