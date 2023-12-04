package com.amikostudio.retrofit



data class Common(
    val message: String,
    val statusCode: Int,
    var data :String

)

data class RegisterResponse(
    val `data`: Profile,
    val message: String,
    val statusCode: Int
)

data class Profile(
    val created_at: String,
    val device_token: String,
    val device_type: Int,
    val email: String,
    val id: Int,
    val is_verified: Int,
    val otp: String,
    val phone_no: Any,
    val remember_token: Any,
    val role: Int,
    val user_image: Any,
    val user_name: String
)

data class AminoListResponse(

    val anime_list: List<Anime>
)

data class Anime(
    val created_at: String,
    val duration: String,
    val embed_url: String,
    val episodes: String,
    val favorites: String,
    val id: Int,
    val image: String,
    val members: String,
    val popularity: String,
    val rank: String,
    val rating: String,
    val score: String,
    val season: String,
    val source: String,
    val status: String,
    val synopsis: String,
    val title: String,
    val title_japanese: String,
    val type: String,
    val updated_at: String,
    val year: String
)


data class AmiakoDetailResponse(
    val anime_list: AnimeList
)

data class AnimeList(
    val created_at: String,
    val `data`: List<EposideList>,
    val duration: String,
    val embed_url: Any,
    val episodes: String,
    val favorites: String,
    val id: Int,
    val image: String,
    val is_favourite: Int,
    val is_saved: Int,
    val is_watched_later: Int,
    val members: String,
    val popularity: String,
    val rank: String,
    val rating: String,
    val score: String,
    val season: Any,
    val source: String,
    val status: String,
    val synopsis: String,
    val title: String,
    val title_japanese: String,
    val type: String,
    val updated_at: String,
    val year: Any
)

data class EposideList(
    val embed_url: String,
    val id: Int,
    val image: String,
    val title: String,
    val title_japanese: String
)




data class WatchLaterListResponse(
    val `data`: List<WatchLaterListData>,
    val statusCode: Int
)

data class WatchLaterListData(
    val anime_id: String,
    val created_at: String,
    val embed_url: String,
    val id: Int,
    val image: String,
    val title: String,
    val title_japanese: String,
    val updated_at: String,
    val user_id: String
)


data class getProfileResponse(
    val `data`: getProfilData,
    val message: String,
    val statusCode: Int
)

data class getProfilData(
    val email: String,
    val user_image: Any,
    val image_path: String,
    val user_name: String
)


data class EditProfileResponse(
    val `data`: editData,
    val message: String,
    val statusCode: Int
)

data class editData(
    val id: Int,
    val user_image: String,
    val user_name: String,
    val image_path: String,
)











