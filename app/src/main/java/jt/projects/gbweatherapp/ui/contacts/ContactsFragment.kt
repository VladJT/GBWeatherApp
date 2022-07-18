package jt.projects.gbweatherapp.ui.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentContactsBinding
import jt.projects.gbweatherapp.utils.showSnackBarShort

const val REQUEST_CODE = 42

class ContactsFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ContactsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // эта строчка говорит о том, что у фрагмента должен быть доступ к меню Активити
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.subtitle = "Список контактов"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            //1) Проверяем случай, если разрешение на доступ к контактам уже дано. Если это так, то
            //вызываем Content Provider для получения контактов. Как мы уже писали выше, потребуется
            //каждый раз проверять разрешение, потому что в любой момент пользователь может его
            //отменить.
            getContacts()
        } else

        //2) Опционально: Отображаем пояснение перед запросом разрешения. Вызываем этот метод, если считаем
        //важным пояснить пользователю приложения, зачем нам требуется доступ к данным на
        //смартфоне. Обычно в роли пояснения выступает диалоговое окно, всплывающее перед тем,
        //как запрашивать доступ
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Доступ к контактам")
                    .setMessage("Прошу дать доступ...")
                    .setPositiveButton("Предоставить доступ") { _, _ ->
                        requestPermission()
                    }
                    .setNegativeButton("Не надо") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else {
                //3) Если разрешения нет и объяснение отображать не надо, запрашиваем разрешение.
                requestPermission()
            }
    }

    private fun requestPermission() {
        // метод запроса разрешений принимает список разрешений — можно запрашивать сразу
        //несколько — и request code, по которому будет определять разрешения.
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            getContacts()
        }
    }

    private fun getContacts() {
        context?.let {
            // Чтобы воспользоваться Content Provider, надо получить инстанс класса ContentResolver и передать
            //в запрос строку URI для этого Provider. Android сам поймёт, какой Provider понадобится вызвать.
            //Чтобы получить список контактов на телефоне, надо передать URI
            //content://com.android.contacts/contacts или воспользоваться константой
            //ContactsContract.Contacts.CONTENT_URI.
            val contentResolver = it.contentResolver
            // Отправляем запрос на получение контактов и получаем ответ в виде Cursor
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursorWithContacts?.let { cursor ->
                for (i in 0 until cursor.count) {
                    // Переходим на позицию в Cursor
                    if (cursor.moveToPosition(i)) {
                        // Берём из Cursor столбец с именем
                        val name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                        val contactView = binding.containerForContacts.addView(AppCompatTextView(it).apply {
                            text = name
                            textSize = 16f
                            setOnClickListener { binding.root.showSnackBarShort(name) }
                        })

                        // divider
                        binding.containerForContacts.addView(AppCompatTextView(it).apply {
                           height = 2
                           setBackgroundColor(R.color.black)
                        })
                    }
                }
            }
            cursorWithContacts?.close()
        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}