package com.example.testapp.data.model

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

data class UserDetailsResponse(
	val lastName: String? = null,
	val gender: String? = null,
	val university: String? = null,
	val maidenName: String? = null,
	val ein: String? = null,
	val ssn: String? = null,
	val bloodGroup: String? = null,
	val password: String? = null,
	val hair: Hair_? = null,
	val bank: Bank_? = null,
	val eyeColor: String? = null,
	val company: Company_? = null,
	val id: Int? = null,
	val email: String? = null,
	val height: Int? = null,
	val image: String? = null,
	val address: Address_? = null,
	val ip: String? = null,
	val weight: Double? = null,
	val userAgent: String? = null,
	val birthDate: String? = null,
	val firstName: String? = null,
	val macAddress: String? = null,
	val phone: String? = null,
	val domain: String? = null,
	val age: Int? = null,
	val username: String? = null
)

data class Bank_(
	val iban: String? = null,
	val cardExpire: String? = null,
	val cardType: String? = null,
	val currency: String? = null,
	val cardNumber: String? = null
)

data class Address_(
	val address: String? = null,
	val city: String? = null,
	val postalCode: String? = null,
	val coordinates: Coordinates_? = null,
	val state: String? = null
)

data class Coordinates_(
	val lng: Double? = null,
	val lat: Double? = null
)

data class Company_(
	val address: Address_? = null,
	val name: String? = null,
	val department: String? = null,
	val title: String? = null
)

data class Hair_(
	val color: String? = null,
	val type: String? = null
)

@BindingAdapter("app:imageUser_" , "app:progressUser_")
fun setUserImage(image: ImageView, imageUrl: String?, progressUser : ProgressBar) {
	val url =  imageUrl
	Glide.with(image.context)
		.load(url)
		.listener(object: RequestListener<Drawable> {
			override fun onLoadFailed(
				e: GlideException?,
				model: Any?,
				target: Target<Drawable>?,
				isFirstResource: Boolean
			): Boolean {
				progressUser.visibility = View.GONE
				return false
			}

			override fun onResourceReady(
				resource: Drawable?,
				model: Any?,
				target: Target<Drawable>?,
				dataSource: DataSource?,
				isFirstResource: Boolean
			): Boolean {
				progressUser.visibility = View.GONE
				return false
			}

		})
		.diskCacheStrategy(DiskCacheStrategy.ALL)
		.centerCrop()
		.into(image)

}

