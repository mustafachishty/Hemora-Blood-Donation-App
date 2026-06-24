package com.example.data.api

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class Part(val text: String)

@JsonClass(generateAdapter = true)
data class Content(val parts: List<Part>)

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(val content: Content)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(val candidates: List<Candidate>?)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }
}

class GeminiRepository {
    suspend fun getMapsGroundedResponse(prompt: String): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return "Please configure your GEMINI_API_KEY in the AI Studio Secrets panel to enable real-time maps-grounded lookup."
        }

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(prompt)))),
            systemInstruction = Content(
                parts = listOf(
                    Part(
                        "You are the Hemora AI Assistant. Your goal is to provide accurate, " +
                        "maps-grounded information on blood donation, blood compatibility, and locate " +
                        "blood donation centers/blood banks in specific cities. Use Google Search and " +
                        "Maps grounding where relevant to list real hospitals, medical centers, " +
                        "and blood banks with names, physical addresses, and contact numbers. " +
                        "Be professional, clear, and structure your responses with Markdown lists."
                    )
                )
            )
        )

        return try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            val candidates = response.candidates
            if (!candidates.isNullOrEmpty()) {
                candidates.first().content.parts.firstOrNull()?.text ?: "No text content returned."
            } else {
                "Unable to retrieve a response. Please check your query and try again."
            }
        } catch (e: Exception) {
            "An error occurred: ${e.message}\n(Make sure your internet is connected and your API Key is verified.)"
        }
    }
}
