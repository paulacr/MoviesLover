package net.paulacr.movieslover.ui.movieslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import net.paulacr.movieslover.R
import net.paulacr.movieslover.data.model.MovieWithGenres
import net.paulacr.movieslover.ui.moviedetail.MovieListener

class MoviesListAdapter(moviesList: List<MovieWithGenres>, private val listener: MovieListener) :
    RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder>() {

    private val moviesList = moviesList.toMutableList()

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): MovieViewHolder {
        val view = LayoutInflater.from(container.context).inflate(R.layout.movie_list_item, container, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(moviesList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, moviesList[position])
        }
    }

    fun updateItems(moviesWithGenres: List<MovieWithGenres>) {
        moviesWithGenres.sortedBy {
            it.movie.popularity
        }
        moviesList.addAll(moviesWithGenres)
        this.notifyItemRangeInserted(itemCount, moviesWithGenres.size - 1)
    }

    fun clearList() {
        moviesList.clear()
        notifyDataSetChanged()
    }

    fun showItemsBySearch(moviesWithGenres: List<MovieWithGenres>) {
        val page = moviesWithGenres.last().movie.page
        if (page == 1) {
            moviesList.clear()
        }
        moviesList.addAll(moviesWithGenres)
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val moviePoster: ImageView = itemView.findViewById(R.id.moviePoster)
        private val title: TextView = itemView.findViewById(R.id.movieTitle)
        private val genres: TextView = itemView.findViewById(R.id.movieGenres)
        private val popularity: TextView = itemView.findViewById(R.id.moviePopularityScore)
        private val releaseYear: TextView = itemView.findViewById(R.id.movieReleaseYear)

        fun bind(movie: MovieWithGenres) {
            Glide.with(itemView.context)
                .load(movie.posterPathUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(moviePoster)

            title.text = movie.movie.title
            popularity.text = itemView.context.getString(R.string.popularity, movie.movie.popularity.toString())
            releaseYear.text = itemView.context.getString(R.string.release_date, movie.movie.releaseDate)
            genres.text = movie.genresList()
        }
    }
}