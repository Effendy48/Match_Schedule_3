package com.example.lenovo.match_schedule.model

import com.google.gson.annotations.SerializedName

data class MatchResponse(
        @SerializedName("events") var matchs: List<Match>
)