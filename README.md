# Shop 'em All

Shop 'em All is final project of the Android Developer Nanodegree program (AKA Capstone project 2 "Build").

It's e-Commerce-like project built leveraging the Firebase technologies. Including Firebase Real-Time database and Firebase Authentication.
Alongside the Android Architecture Components: Room, ViewModel, and LiveData. And set of other libraries like ButterKnife, Glide, and Stripe.

You can find the file containing the database under the name of `shop-em-all-export.json`. This file holds dummy shopping `Categories`. Each `category` has a set of fake `products` and prices.

Every product has set of `attributes` like `name`, `description`, `image`, `code`, `price` and `sale`. For the `code` it's just random numbers, but in real life, it should be a unique code like `UPC`.
For the `price` and `sale` IF any, the sale price is subtracted from the `product price` and the `sale percentage` will be shown on a label on top of the `product image`.

The user can add any product to his `Cart` to check out later, to increase the quantity, decrease it, or to add the `product` to his `Wishlist`. He can view his `Wishlist`products either in the `WishlistActivity` or by the app `Widget`.
For ordering the user have to insert some of his details, like `name`, `address`, `e-mail`, `phone number` and his `credit card` details.
Each `order` has it's `orderStatus` defined as follows: `1- process 2- packed 3- shipped 4- arrived (in the country) 5- delivered (to the user)`.

| MainActivity  |CategoryProductsActivity| CartActivity |
| ------------- |--------------| ------------- |
| ![MainActivity](https://github.com/AbduallahAtta/ShopemAll/blob/master/screenshots/Screenshot_20180810-051756.png)| ![CategoryProductsActivity](https://github.com/AbduallahAtta/ShopemAll/blob/master/screenshots/Screenshot_20180810-051833.png)| ![CartActivity](https://github.com/AbduallahAtta/ShopemAll/blob/master/screenshots/Screenshot_20180810-051922.png)
-------
**Project features:**
* Explore online shopping categories.
* Explore online shopping products.
* Search for a specific product within a specific category.
* Share product details.
* Add products to Shopping Cart.
* Increase or Decrease product quantity in the Shopping Cart.
* Add products to the Wishlist. 
* View products in the Wishlist from the App Widget.
* Order products from the Shopping Cart.
* View previous orders and track them.

-------
**Third Party Libraries used in the project:**
* [Support Library](https://developer.android.com/topic/libraries/support-library/packages)
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)
* [Firebase database](https://firebase.google.com/docs/database/)
* [Firebase Authentication](https://firebase.google.com/docs/auth/)
* [FirebaseUI](https://github.com/firebase/FirebaseUI-Android)
* [ButterKnife](http://jakewharton.github.io/butterknife/)
* [Glide](https://bumptech.github.io/glide/)
* [ShapeOfView](https://github.com/florent37/ShapeOfView)
* [KenBurnsView](https://github.com/flavioarfaria/KenBurnsView)
* [TransitionsEveryWhere](https://github.com/andkulikov/Transitions-Everywhere)
* [Stripe](https://stripe.com/docs/mobile/android)
* [CountryCodePicker](https://github.com/hbb20/CountryCodePickerProject)
* [CardForm](https://github.com/braintree/android-card-form)
* [LabelView](https://github.com/linger1216/labelview)
* [MaterialSearchView](https://github.com/MiguelCatalan/MaterialSearchView)
* [Alerter](https://github.com/Tapadoo/Alerter)
* [CardSlider](https://github.com/Ramotion/cardslider-android)
* [ParallaxEverywhere](https://github.com/Narfss/ParallaxEverywhere)
* [TimeLineView](https://github.com/vipulasri/Timeline-View)
* [FABMenu](https://github.com/Clans/FloatingActionButton)

------
# License
MIT License

Copyright (c) 2018 Abdullah Atta Amien

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
