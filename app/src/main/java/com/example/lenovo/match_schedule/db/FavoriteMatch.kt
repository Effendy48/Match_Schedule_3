package com.example.lenovo.match_schedule.db

data class FavoriteMatch (val Id : Long? ,val idEvent : String?, val teamHomeId : String?,
                          val teamAwayId : String?, /*val nameTeamHome : String? */
                          /*val nameTeamAway : String? */ val imageTeamHome : String?,
                          val imageTeamAway : String?, val scoreTeamHome : String?,
                          val scoreTeamAway: String?, val dateEvent : String){
    companion object {
        const val TABLE_FAVORITE_MATCH : String = "TABLE_FAVORITE_MATCH"
        const val ID : String = "ID_"
        const val ID_EVENT : String = "ID_EVENT"
        const val TEAM_HOME_ID : String = "TEAM_HOME_ID"
        const val TEAM_AWAY_ID : String = "TEAM_AWAY_ID"
       // const val TEAM_HOME_NAME : String = "TEAM_HOME_NAME"
        //const val TEAM_AWAY_NAME : String = "TEAM_AWAY_NAME"
        const val TEAM_HOME_IMAGE : String = "TEAM_HOME_IMAGE"
        const val TEAM_AWAY_IMAGE : String = "TEAM_AWAY_IMAGE"
        const val TEAM_HOME_SCORE : String = "TEAM_HOME_SCORE"
        const val TEAM_AWAY_SCORE : String = "TEAM_AWAY_SCORE"
        const val DATE_EVENT : String = "DATE_EVENT"

    }
}