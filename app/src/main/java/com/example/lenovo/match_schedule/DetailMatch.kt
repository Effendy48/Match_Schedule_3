package com.example.lenovo.match_schedule

import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem
import com.example.lenovo.match_schedule.R.drawable.ic_add_to_favorites
import com.example.lenovo.match_schedule.R.drawable.ic_added_to_favorites
import com.example.lenovo.match_schedule.R.id.add_to_favorite
import com.example.lenovo.match_schedule.R.menu.detail_menu
import com.example.lenovo.match_schedule.api.ApiInterface
import com.example.lenovo.match_schedule.api.ApiRepository
import com.example.lenovo.match_schedule.db.FavoriteMatch
import com.example.lenovo.match_schedule.db.database
import com.example.lenovo.match_schedule.model.DetailTeam
import com.example.lenovo.match_schedule.model.DetailTeamResponse
import com.example.lenovo.match_schedule.model.Match
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_match.*
import kotlinx.android.synthetic.main.mtrl_layout_snackbar_include.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Response

import javax.security.auth.callback.Callback

class DetailMatch : AppCompatActivity() {
   // private lateinit var swipeRefresh: SwipeRefreshLayout


    private lateinit var idHome: String
    private lateinit var idAway: String
    private lateinit var goalAway: String
    private lateinit var goalHome: String
    private lateinit var defenceAway: String
    private lateinit var defenceHome: String
    private lateinit var midfieldAway: String
    private lateinit var midfieldHome: String
    private lateinit var forwardHome: String
    private lateinit var forwardAway: String
    private lateinit var subtitutiesHome: String
    private lateinit var subtitutiesAway: String

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var id: String

    companion object {
        val POSITION_EXTRA = "position_extra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_match)

        val detail = intent.getParcelableExtra<Match>(POSITION_EXTRA)
        id = detail.idEvent.toString()

     //   swipeRefresh = findViewById(R.id.swipeRefresh)

        supportActionBar!!.setTitle("Match Detail")
        Detail()
        loadImageTeamAway()
        loadImageTeamHome()
        favoriteState()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean{
        return when (item.itemId){
            add_to_favorite ->{
                if(isFavorite) removeFromFavorite() else addToFavorite()

                    isFavorite = !isFavorite
                    setFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun Detail() {
        val detail = intent.getParcelableExtra<Match>(POSITION_EXTRA)
        idHome = detail.idHomeTeam.toString()
        idAway = detail.idAwayTeam.toString()

        goalAway = detail.strAwayGoalDetails.toString().split(";").toString()
                .replace("[", "").trim()
                .replace("]", "").trim()
                .replace(", ", "; \n").trim()
                .replace(",", ";").trim()

        goalHome = detail.strHomeGoalDetails.toString().split(";").toString()
                .replace("[", "").trim()
                .replace("]", "").trim()
                .replace(", ", "; \n").trim()
                .replace(",", ";").trim()
        defenceAway = detail.strAwayLineupDefense.toString().split(";").toString()
                .replace("[", "").trim()
                .replace("]", "").trim()
                .replace(", ", " \n").trim()
                .replace(",", ".").trim()
        defenceHome = detail.strHomeLineupDefense.toString().split(";").toString()
                .replace("[", "").trim()
                .replace("]", "").trim()
                .replace(", ", ". \n").trim()
                .replace(",", ".").trim()
        midfieldAway = detail.strAwayLineupMidfield.toString().split(";").toString()
                .replace("[", "").trim()
                .replace("]", "").trim()
                .replace(", ", ". \n").trim()
                .replace(",", ".").trim()
        midfieldHome = detail.strHomeLineupMidfield.toString().split(";").toString()
                .replace("[", "").trim()
                .replace("]", "").trim()
                .replace(", ", ". \n").trim()
                .replace(",", ".").trim()
        forwardAway = detail.strAwayLineupForward.toString().split(";").toString()
                .replace("[", "").trim()
                .replace("]", "").trim()
                .replace(", ", ". \n").trim()
                .replace(",", ".").trim()
        forwardHome = detail.strHomeLineupForward.toString().split(";").toString()
                .replace("[", "").trim()
                .replace("]", "").trim()
                .replace(", ", ". \n").trim()
                .replace(",", ".").trim()

        subtitutiesAway = detail.strAwayLineupSubstitutes.toString().split(";").toString()
                .replace("[", "").trim()
                .replace("]", "").trim()
                .replace(", ", ". \n").trim()
                .replace(",", ".").trim()
        subtitutiesHome = detail.strHomeLineupSubstitutes.toString().split(";").toString()
                .replace("[", "").trim()
                .replace("]", "").trim()
                .replace(", ", ". \n").trim()
                .replace(",", ".").trim()

        tv_date_detail_match.text = detail.dateEvent.toString()

        if (detail.strHomeLineupDefense != null) tv_defence_home.text = defenceHome.trim() else tv_defence_home.text = "Tidak Ada"
        if (detail.strAwayLineupDefense != null) tv_defence_away.text = defenceAway.trim() else tv_defence_away.text = "Tidak Ada"

        if (detail.strAwayLineupMidfield != null) tv_midfield_away.text = midfieldAway.trim() else tv_midfield_away.text = "Tidak Ada"
        if (detail.strHomeLineupMidfield != null) tv_midfield_home.text = midfieldHome.trim() else tv_midfield_home.text = "Tidak Ada"

        if (detail.strAwayLineupForward != null) tv_forward_away.text = forwardAway.trim() else tv_forward_away.text = "Tidak Ada"
        if (detail.strHomeLineupForward != null) tv_forward_home.text = forwardHome.trim() else tv_forward_home.text = "Tidak Ada"

        if (detail.strHomeGoalDetails == null) tv_goals_home.text = "Tidak Ada" else tv_goals_home.text = goalHome.trim()
        if (detail.strAwayGoalDetails == null) tv_goals_away.text = "Tidak Ada" else tv_goals_away.text = goalAway.trim()

        if (detail.intAwayScore == null) tv_away_score.text = "-" else tv_away_score.text = detail.intAwayScore.toString()
        if (detail.intHomeScore == null) tv_home_score.text = "-" else tv_home_score.text = detail.intHomeScore.toString()

        if (detail.strAwayLineupGoalkeeper == null) tv_goalkeeper_away.text = detail.strAwayLineupGoalkeeper.toString() else tv_goalkeeper_away.text = "Tidak Ada"
        if (detail.strHomeLineupGoalkeeper != null) tv_goalkeeper_home.text = detail.strHomeLineupGoalkeeper.toString() else tv_goalkeeper_home.text = "Tidak Ada"

        if (detail.strAwayLineupSubstitutes != null || detail.strAwayLineupSubstitutes == "")
            tv_subtitutes_away.text = subtitutiesAway.trim()
        else tv_subtitutes_away.text = "Tidak Ada"

        if (detail.strHomeLineupSubstitutes != null || detail.strHomeLineupSubstitutes == "")
            tv_subtitutes_home.text = subtitutiesHome.trim()
        else tv_subtitutes_home.text = "Tidak Ada"
    }

    private fun loadImageTeamHome() {
        val search = ApiRepository.getRetrofit().create(ApiInterface::class.java)
        val call = search.getTeamHome(idHome)
        call.enqueue(object : retrofit2.Callback<DetailTeamResponse> {
            override fun onFailure(call: Call<DetailTeamResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<DetailTeamResponse>, response: Response<DetailTeamResponse>) {
                if (response.isSuccessful()) {
                    val detailTeam: List<DetailTeam> = response.body()?.teams!!
                    for (item: DetailTeam? in detailTeam.iterator()) {
                        Picasso.get().load(item?.strTeamBadge).into(img_home_detail_match)
                    }
                }
            }

        })

    }

    private fun loadImageTeamAway() {
        val search = ApiRepository.getRetrofit().create(ApiInterface::class.java)
        val call = search.getTeamHome(idAway)
        call.enqueue(object : retrofit2.Callback<DetailTeamResponse> {
            override fun onFailure(call: Call<DetailTeamResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<DetailTeamResponse>, response: Response<DetailTeamResponse>) {
                if (response.isSuccessful()) {
                    val detailTeam: List<DetailTeam> = response.body()?.teams!!
                    for (item: DetailTeam? in detailTeam.iterator()) {
                        Picasso.get().load(item?.strTeamBadge).into(img_away_detail_match)
                    }
                }
            }

        })

    }
    private fun setFavorite(){
        if(isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this,ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorites)
    }

    private fun favoriteState(){
        database.use {
            val result = select(FavoriteMatch.TABLE_FAVORITE_MATCH)
                    .whereArgs("ID_EVENT = {id}", "id" to id)
            val favoriteMatch = result.parseList(classParser<FavoriteMatch>())
            if(!favoriteMatch.isEmpty()) isFavorite = true
        }
    }
    private fun addToFavorite(){
        val detail = intent.getParcelableExtra<Match>(POSITION_EXTRA)

        try {
            database.use {
                insert(FavoriteMatch.TABLE_FAVORITE_MATCH,
                        FavoriteMatch.TEAM_HOME_ID to detail.idHomeTeam.toString(),
                        FavoriteMatch.TEAM_AWAY_ID to detail.idAwayTeam.toString(),
                        FavoriteMatch.TEAM_AWAY_SCORE to detail.intAwayScore.toString(),
                        FavoriteMatch.TEAM_AWAY_IMAGE to loadImageTeamAway().toString(),


                        FavoriteMatch.TEAM_HOME_ID to detail.idHomeTeam.toString(),
                        FavoriteMatch.TEAM_HOME_SCORE to detail.intHomeScore.toString(),
                        FavoriteMatch.TEAM_HOME_IMAGE to loadImageTeamHome().toString()
                        )
            }
            toast("Added to Favorite").show()
        }catch (e : SQLiteConstraintException){
           toast(e.localizedMessage).show()
        }
    }
    private fun removeFromFavorite(){
        try {
            database.use {
                delete(FavoriteMatch.TABLE_FAVORITE_MATCH, "(ID_EVENT = {idEvent})", "idEvent" to id)
            }
            toast("Remove to Favorite").show()
        }catch (e : SQLiteConstraintException){
            toast(e.localizedMessage).show()

        }
    }
}
