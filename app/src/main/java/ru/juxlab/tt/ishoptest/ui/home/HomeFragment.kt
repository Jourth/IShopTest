package ru.juxlab.tt.ishoptest.ui.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.constraint.Group
import android.support.design.card.MaterialCardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.launch
import ru.juxlab.tt.ishoptest.R
import ru.juxlab.tt.ishoptest.data.FlashSale
import ru.juxlab.tt.ishoptest.data.Latest
import ru.juxlab.tt.ishoptest.data.ProductCategory
import ru.juxlab.tt.ishoptest.ui.ScopedFragment
import java.net.URL
import java.text.NumberFormat
import java.util.concurrent.Executors


class HomeFragment : ScopedFragment(), AdapterView.OnItemClickListener {

    private val viewModelFactory = HomeFragmentViewModelFactory()
    private lateinit var viewModel: HomeFragmentViewModel

    private val itemClickListener = this

    private lateinit var recyclerViewCategories: RecyclerView
    private lateinit var viewAdapterCategories: RecyclerView.Adapter<*>
    private lateinit var viewManagerCategories: RecyclerView.LayoutManager

    private lateinit var recyclerViewLatest: RecyclerView
    private lateinit var viewAdapterLatest: RecyclerView.Adapter<*>
    private lateinit var viewManagerLatest: RecyclerView.LayoutManager

    private lateinit var recyclerViewFlashSale: RecyclerView
    private lateinit var viewAdapterFlashSale: RecyclerView.Adapter<*>
    private lateinit var viewManagerFlashSale: RecyclerView.LayoutManager

    private lateinit var recyclerViewBrands: RecyclerView
    private lateinit var viewAdapterBrands: RecyclerView.Adapter<*>
    private lateinit var viewManagerBrands: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeFragmentViewModel::class.java)
        val groupContent = activity!!.findViewById<Group>(R.id.group_content)
        groupContent.visibility = View.GONE
        bindUI()

    }

    private fun bindUI() = launch {
        val latestProductsList = viewModel.latestProductsList.await().await()
        val flashSaleList = viewModel.flashSaleList.await().await()
        val categoriesList = ProductCategory.getProductCategories()



        viewAdapterLatest = LatestListAdapter(latestProductsList.latest, itemClickListener, activity?.applicationContext!!)
        viewManagerLatest = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        recyclerViewLatest = activity!!.findViewById<RecyclerView>(R.id.recyclerView_latest).apply {
            setHasFixedSize(true)
            layoutManager = viewManagerLatest
            adapter = viewAdapterLatest
        }

        viewAdapterFlashSale = FlashSaleListAdapter(flashSaleList.flashSale, itemClickListener, activity?.applicationContext!!)
        viewManagerFlashSale = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        recyclerViewFlashSale = activity!!.findViewById<RecyclerView>(R.id.recyclerView_flash_sale).apply {
            setHasFixedSize(true)
            layoutManager = viewManagerFlashSale
            adapter = viewAdapterFlashSale
        }

        viewAdapterBrands = BrandsListAdapter(latestProductsList.latest, itemClickListener, activity?.applicationContext!!)
        viewManagerBrands = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        recyclerViewFlashSale = activity!!.findViewById<RecyclerView>(R.id.recyclerView_brands).apply {
            setHasFixedSize(true)
            layoutManager = viewManagerBrands
            adapter = viewAdapterBrands
        }

        viewAdapterCategories = CategoryListAdapter(categoriesList, itemClickListener, activity?.applicationContext!!)
        viewManagerCategories = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        recyclerViewCategories = activity!!.findViewById<RecyclerView>(R.id.recyclerView_categories).apply {
            setHasFixedSize(true)
            layoutManager = viewManagerCategories
            adapter = viewAdapterCategories
        }

        val groupLoading = activity!!.findViewById<Group>(R.id.group_loading)
        groupLoading.visibility = View.GONE
        val groupContent = activity!!.findViewById<Group>(R.id.group_content)
        groupContent.visibility = View.VISIBLE

    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

}

class CategoryListAdapter(private val myDataset: List<ProductCategory>, private val itemClickListener: AdapterView.OnItemClickListener, private val context: Context) :
    RecyclerView.Adapter<CategoryListAdapter.ObjectsListViewHolder>() {

    class ObjectsListViewHolder(private val cardView: MaterialCardView) : RecyclerView.ViewHolder(cardView){
        private val categoryNameView = cardView.findViewById<TextView>(R.id.textView_category_name)
        private val categoryImageView = cardView.findViewById<ImageView>(R.id.imageView_category_icon)
        fun bind(productCategory: ProductCategory, clickListener: AdapterView.OnItemClickListener) {
            categoryNameView.setText(productCategory.nameId)
            categoryImageView.setImageResource(productCategory.iconId)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ObjectsListViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_card, parent, false) as MaterialCardView

        return ObjectsListViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ObjectsListViewHolder, position: Int) {
        val productCategory = myDataset[position]
        holder.bind(productCategory, itemClickListener)


    }
    override fun getItemCount() = myDataset.size
}

class LatestListAdapter(private val myDataset: List<Latest>, private val itemClickListener: AdapterView.OnItemClickListener, private val context: Context) :
    RecyclerView.Adapter<LatestListAdapter.ObjectsListViewHolder>() {

    class ObjectsListViewHolder(private val cardView: MaterialCardView) : RecyclerView.ViewHolder(cardView){
        private val productNameView = cardView.findViewById<TextView>(R.id.textView_product_name)
        private val categoryNameView = cardView.findViewById<TextView>(R.id.textView_category_name)
        private val priceView = cardView.findViewById<TextView>(R.id.textView_product_price)
        private val productImageView = cardView.findViewById<ImageView>(R.id.imageView_product_image)

        fun bind(latest: Latest, clickListener: AdapterView.OnItemClickListener) {
            productNameView.text = latest.name
            categoryNameView.text = latest.category

            val formatter: NumberFormat = NumberFormat.getNumberInstance()
            formatter.isGroupingUsed = true
            formatter.maximumFractionDigits = 0
            priceView.text = "$ " + formatter.format(latest.price)

            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())

            executor.execute {
                try{
                    val bitmap = BitmapFactory.decodeStream(URL(latest.imageUrl).openStream())
                    handler.post {
                        productImageView.setImageBitmap(bitmap)
                    }
                }
                catch (e: Exception) {
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ObjectsListViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.latest_product_card, parent, false) as MaterialCardView

        return ObjectsListViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ObjectsListViewHolder, position: Int) {
        val latest = myDataset[position]
        holder.bind(latest, itemClickListener)


    }
    override fun getItemCount() = myDataset.size
}

class FlashSaleListAdapter(private val myDataset: List<FlashSale>, private val itemClickListener: AdapterView.OnItemClickListener, private val context: Context) :
    RecyclerView.Adapter<FlashSaleListAdapter.ObjectsListViewHolder>() {

    class ObjectsListViewHolder(private val cardView: MaterialCardView) : RecyclerView.ViewHolder(cardView){
        private val productNameView = cardView.findViewById<TextView>(R.id.textView_product_name)
        private val categoryNameView = cardView.findViewById<TextView>(R.id.textView_category_name)
        private val priceView = cardView.findViewById<TextView>(R.id.textView_product_price)
        private val discountView = cardView.findViewById<TextView>(R.id.textView_discount)
        private val productImageView = cardView.findViewById<ImageView>(R.id.imageView_product_image)


        //private var objectName : String = nameView.text.toString()

        fun bind(flashSale: FlashSale, clickListener: AdapterView.OnItemClickListener) {
            productNameView.text = flashSale.name
            categoryNameView.text = flashSale.category

            val formatter: NumberFormat = NumberFormat.getNumberInstance()
            formatter.isGroupingUsed = true
            formatter.maximumFractionDigits = 2
            formatter.minimumFractionDigits = 2
            priceView.text = "$ " + formatter.format(flashSale.price)

            val formatterDiscount: NumberFormat = NumberFormat.getPercentInstance()
            formatter.isGroupingUsed = true
            formatter.maximumFractionDigits = 0
            formatter.minimumFractionDigits = 0
            discountView.text = formatter.format(flashSale.discount) + cardView.resources.getString(R.string.discount_postfix)

            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())

            executor.execute {
                try{
                    val bitmap = BitmapFactory.decodeStream(URL(flashSale.imageUrl).openStream())
                    handler.post {
                        productImageView.setImageBitmap(bitmap)
                    }
                }
                catch (e: Exception) {
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ObjectsListViewHolder {

        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.flash_sale_card, parent, false) as MaterialCardView

        return ObjectsListViewHolder(cardView)
    }


    override fun onBindViewHolder(holder: ObjectsListViewHolder, position: Int) {

        val flashSale = myDataset[position]
        holder.bind(flashSale, itemClickListener)


    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

}

class BrandsListAdapter(private val myDataset: List<Latest>, private val itemClickListener: AdapterView.OnItemClickListener, private val context: Context) :
    RecyclerView.Adapter<BrandsListAdapter.ObjectsListViewHolder>() {

    class ObjectsListViewHolder(private val cardView: MaterialCardView) : RecyclerView.ViewHolder(cardView){
        private val productNameView = cardView.findViewById<TextView>(R.id.textView_product_name)
        private val categoryNameView = cardView.findViewById<TextView>(R.id.textView_category_name)
        private val priceView = cardView.findViewById<TextView>(R.id.textView_product_price)
        private val productImageView = cardView.findViewById<ImageView>(R.id.imageView_product_image)

        fun bind(latest: Latest, clickListener: AdapterView.OnItemClickListener) {
            productNameView.text = latest.name
            categoryNameView.text = latest.category
            priceView.text = latest.price.toString()

            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())

            executor.execute {
                try{
                    val bitmap = BitmapFactory.decodeStream(URL(latest.imageUrl).openStream())
                    handler.post {
                        productImageView.setImageBitmap(bitmap)
                    }
                }
                catch (e: Exception) {
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ObjectsListViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.latest_product_card, parent, false) as MaterialCardView

        return ObjectsListViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ObjectsListViewHolder, position: Int) {
        val latest = myDataset[position]
        holder.bind(latest, itemClickListener)


    }
    override fun getItemCount() = myDataset.size
}

