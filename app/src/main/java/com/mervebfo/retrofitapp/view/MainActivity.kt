package com.mervebfo.retrofitapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mervebfo.retrofitapp.R
import com.mervebfo.retrofitapp.adapter.RecyclerViewAdapter
import com.mervebfo.retrofitapp.databinding.ActivityMainBinding
import com.mervebfo.retrofitapp.model.CryptoModel
import com.mervebfo.retrofitapp.service.CryptoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory



class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {
    private val BASE_URL="https://api.nomics.com/v1/"
    private var cryptoModels:ArrayList<CryptoModel>? =null
    private var recyclerViewAdapter:RecyclerViewAdapter?=null
    lateinit var binding: ActivityMainBinding
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        compositeDisposable= CompositeDisposable()

        val layoutManager:RecyclerView.LayoutManager=LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager
        loadData()
    }

    private fun loadData(){
        val retrofit=Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)

        compositeDisposable?.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))


        /*
        val service=retrofit.create(CryptoAPI::class.java)

        val call=service.getData()
        call.enqueue(object: Callback<List<CryptoModel>> {

            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        cryptoModels= ArrayList(it)

                        cryptoModels?.let{
                            recyclerViewAdapter=RecyclerViewAdapter(it,this@MainActivity)
                            binding.recyclerView.adapter=recyclerViewAdapter
                        }



                        /*
                        for (cryptoModel: CryptoModel in cryptoModels!!){
                            println(cryptoModel.currency)
                            println(cryptoModel.price)
                        } */
                    }
                }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        }) */
    }

    private fun handleResponse(cryptoList : List<CryptoModel>){
        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let {
            recyclerViewAdapter = RecyclerViewAdapter(it,this@MainActivity)
            binding.recyclerView.adapter = recyclerViewAdapter
        }
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this,"Clicked: ${cryptoModel.currency}",Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }

}