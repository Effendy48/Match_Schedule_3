package com.example.lenovo.match_schedule.model

import com.google.gson.annotations.SerializedName

data class DetailTeamResponse(
        @SerializedName("teams") var teams: List<DetailTeam>
)