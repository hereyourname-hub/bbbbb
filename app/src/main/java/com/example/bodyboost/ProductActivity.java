package com.example.bodyboost;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.room.Room;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.example.bodyboost.Database.CalorieDAO;
import com.example.bodyboost.Database.CalorieDatabase;
import com.example.bodyboost.Database.CalorieDay;
import com.example.bodyboost.Database.CalorieProduct;
import com.example.bodyboost.Database.ProductDAO;
import com.example.bodyboost.Database.ProductDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity {

    private ProductDAO productDAO;
    private CalorieDAO calorieDAO;
    private final Utilities utilities = new Utilities();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        setTitle("Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProductDatabase productDatabase = Room.databaseBuilder(
                getApplicationContext(),
                ProductDatabase.class,
                "ProductDatabase"
        ).allowMainThreadQueries().build();
        productDAO = productDatabase.productDAO();

        CalorieDatabase calorieDatabase = Room.databaseBuilder(
                getApplicationContext(),
                CalorieDatabase.class,
                "CalorieDatabase"
        ).allowMainThreadQueries().build();
        calorieDAO = calorieDatabase.calorieDAO();

        createTable();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createTable() {
        TableLayout productTable = findViewById(R.id.productTableLayout);
        TableRow headerRow = new TableRow(this);
        List<String> tableHeaders = Arrays.asList(
                "Product",
                "Amount",
                "Prefix",
                "Calories"
        );

        for (int i = 0; i < tableHeaders.size(); i++) {
            TextView headerText = new TextView(this);
            headerText.setText(tableHeaders.get(i));
            headerText.setTextSize(24);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                headerText.setTextColor(getColor(R.color.white));
            }
            headerText.setPadding(15,15,15,15);
            headerText.setBackground(getDrawable(R.drawable.border));
            headerRow.addView(headerText);
        }

        productTable.addView(headerRow);

        populateTable(productTable);
    }

    private void rebuildTable() {
        TableLayout productTable = findViewById(R.id.productTableLayout);
        productTable.removeAllViews();
        createTable();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void populateTable(TableLayout productTable) {
        List<CalorieProduct> products = productDAO.getAll();

        for (int i = 0; i < products.size(); i++) {
            TableRow productRow = new TableRow(this);
            CalorieProduct calorieProduct = products.get(i);

            productRow.setOnClickListener(view -> {
                showProductPopup(calorieProduct);
                Log.d("Test", "populateTable: ");
            });

            TextView productName = new TextView(this);
            productName.setText(calorieProduct.getProductName());
            productName.setTextSize(20);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                productName.setTextColor(getColor(R.color.white));
            }
            productName.setPadding(15,15,15,15);
            productName.setBackground(getDrawable(R.drawable.border));
            productRow.addView(productName);

            TextView productAmount = new TextView(this);
            productAmount.setText(String.valueOf(calorieProduct.getProductAmount()));
            productAmount.setTextSize(20);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                productAmount.setTextColor(getColor(R.color.white));
            }
            productAmount.setPadding(15,15,15,15);
            productAmount.setBackground(getDrawable(R.drawable.border));
            productRow.addView(productAmount);

            TextView amountPrefix = new TextView(this);
            amountPrefix.setText(calorieProduct.getAmountPrefix());
            amountPrefix.setTextSize(20);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                amountPrefix.setTextColor(getColor(R.color.white));
            }
            amountPrefix.setPadding(15,15,15,15);
            amountPrefix.setBackground(getDrawable(R.drawable.border));
            productRow.addView(amountPrefix);

            TextView calorieAmount = new TextView(this);
            calorieAmount.setText(String.valueOf(calorieProduct.getCalorieAmount()));
            calorieAmount.setTextSize(20);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                calorieAmount.setTextColor(getColor(R.color.white));
            }
            calorieAmount.setPadding(15,15,15,15);
            calorieAmount.setBackground(getDrawable(R.drawable.border));
            productRow.addView(calorieAmount);

            productTable.addView(productRow);
        }

        TableRow emptyRow = new TableRow(this);
        TextView invisibleText = new TextView(this);
        invisibleText.setText("Invisible");
        invisibleText.setTextSize(26);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            invisibleText.setTextColor(getColor(R.color.SeeTrough));
        }
        emptyRow.addView(invisibleText);
        productTable.addView(emptyRow);
    }

    private void addProduct(String name, int amount, String amountPrefix, int calorieAmount) {
        CalorieProduct calorieProduct = new CalorieProduct();
        calorieProduct.setProductName(name);
        calorieProduct.setProductAmount(amount);
        calorieProduct.setAmountPrefix(amountPrefix);
        calorieProduct.setCalorieAmount(calorieAmount);

        productDAO.insert(calorieProduct);
    }

    private void editProduct(CalorieProduct calorieProduct) {
        productDAO.update(calorieProduct);
    }

    private void deleteProduct(int calorieProductId) {
        productDAO.deleteById(calorieProductId);
        rebuildTable();
    }

    private void addCalories(int calorieAmount) {
        int newCalorieAmount = calorieAmount;

        if (utilities.todayCalorieDayExists(calorieDAO)) {
            CalorieDay calorieDay = calorieDAO.getLastRecords(1).get(0);
            int currentCalorieAmount = calorieDay.getCalorieAmount();
            newCalorieAmount = currentCalorieAmount + calorieAmount;
            calorieDay.setCalorieAmount(newCalorieAmount);
            calorieDAO.update(calorieDay);
        } else {
            CalorieDay calorieDay = new CalorieDay();
            calorieDay.setCalorieAmount(calorieAmount);
            calorieDay.setCalorieDate(
                    utilities.getCurrentDate(new SimpleDateFormat("y-MM-d", Locale.ENGLISH)
                    )
            );
            calorieDAO.insert(calorieDay);
        }

        View contextView = this.findViewById(android.R.id.content);
        @SuppressLint("DefaultLocale") Snackbar snackbar = Snackbar
                .make(contextView, String.format("New calorie amount: %d", newCalorieAmount), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showProductPopup(CalorieProduct calorieProduct) {
        final Drawable deleteIcon = AppCompatResources.getDrawable(this, R.drawable.delete);
        final Drawable addIcon = AppCompatResources.getDrawable(this, R.drawable.add_one);
        final Drawable editIcon = AppCompatResources.getDrawable(this, R.drawable.edit);

        MaterialAlertDialogBuilder productPopup = new MaterialAlertDialogBuilder(this, R.style.EditPopup)
                .setTitle("Product")
                .setMessage("View product info, modify it or delete it.")
                .setPositiveButtonIcon(addIcon)
                .setPositiveButton("", (dialogInterface, i) -> addCalories(calorieProduct.getCalorieAmount()))
                .setNeutralButtonIcon(deleteIcon)
                .setNeutralButton("", (dialogInterface, i) -> showDeleteProductPopup(calorieProduct.getId()))
                .setNegativeButtonIcon(editIcon)
                .setNegativeButton("", (dialogInterface, i) -> showEditProductPopup(calorieProduct));

        final View dialogView = LayoutInflater.from(productPopup.getContext()).inflate(R.layout.popup_product, null);

        String productName = calorieProduct.productName;
        String productAmount = Integer.toString(calorieProduct.productAmount);
        String productPrefix = calorieProduct.amountPrefix;
        String productCalories = Integer.toString(calorieProduct.calorieAmount);

        TextView nameTextView = dialogView.findViewById(R.id.productName);
        TextView amountTextView = dialogView.findViewById(R.id.productAmount);
        TextView caloriesTextView = dialogView.findViewById(R.id.productCalories);

        nameTextView.setText(productName);
        amountTextView.setText(String.format("%s%s", productAmount, productPrefix));
        caloriesTextView.setText(productCalories);

        productPopup.setView(dialogView);
        productPopup.show();
    }

    public void showDeleteProductPopup(int calorieProductId) {
         new MaterialAlertDialogBuilder(this, R.style.DeletePopup)
                 .setTitle("Are you sure?")
                 .setMessage("View product info, modify it or delete it.")
                 .setPositiveButton("DELETE", (dialogInterface, i) -> deleteProduct(calorieProductId))
                 .setNeutralButton("CANCEL", (dialogInterface, i) -> {})
                 .show();
    }

    public void showAddProductPopup(View view) {
        MaterialAlertDialogBuilder addProductPopup = new MaterialAlertDialogBuilder(this, R.style.RoundShapePopup)
                .setTitle("Create new product")
                .setMessage("Add the required info for the product.")
                .setNeutralButton("CANCEL", (dialogInterface, i) -> { });

        final View dialogView = LayoutInflater.from(addProductPopup.getContext()).inflate(R.layout.popup_add_product, null);

        TextView productNameInput = dialogView.findViewById(R.id.productNameInput);
        TextView productAmountInput = dialogView.findViewById(R.id.productAmountInput);
        TextView productPrefixInput = dialogView.findViewById(R.id.productPrefixInput);
        TextView productCaloriesInput = dialogView.findViewById(R.id.productCaloriesInput);

        addProductPopup.setView(dialogView);
        addProductPopup.setPositiveButton("ADD PRODUCT", (dialogInterface, i) -> {
            String productName = productNameInput.getText().toString();
            String productAmount = productAmountInput.getText().toString();
            String productPrefix = productPrefixInput.getText().toString();
            String productCalories = productCaloriesInput.getText().toString();
            if (productName.isEmpty() || productAmount.isEmpty() || productPrefix.isEmpty() || productCalories.isEmpty()) {
                View contextView = this.findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(
                        contextView,
                        "Couldn't add to database\nPlease make sure you fill every field",
                        Snackbar.LENGTH_LONG
                );
                snackbar.show();
            } else {
                addProduct(productName, Integer.parseInt(productAmount), productPrefix, Integer.parseInt(productCalories));
                rebuildTable();
            }
        });
        addProductPopup.show();
    }

    public void showEditProductPopup(CalorieProduct calorieProduct) {

        MaterialAlertDialogBuilder editProductPopup = new MaterialAlertDialogBuilder(this, R.style.RoundShapePopup)
                .setTitle("Edit product")
                .setMessage("Modify the info for the product.")
                .setNeutralButton("CANCEL", (dialogInterface, i) -> { });

        final View dialogView = LayoutInflater.from(editProductPopup.getContext()).inflate(R.layout.popup_add_product, null);

        TextView productNameInput = dialogView.findViewById(R.id.productNameInput);
        TextView productAmountInput = dialogView.findViewById(R.id.productAmountInput);
        TextView productPrefixInput = dialogView.findViewById(R.id.productPrefixInput);
        TextView productCaloriesInput = dialogView.findViewById(R.id.productCaloriesInput);

        productNameInput.setText(calorieProduct.getProductName());
        productAmountInput.setText(String.valueOf(calorieProduct.getProductAmount()));
        productPrefixInput.setText(calorieProduct.getAmountPrefix());
        productCaloriesInput.setText(String.valueOf(calorieProduct.getCalorieAmount()));

        editProductPopup.setView(dialogView);
        editProductPopup.setPositiveButton("MODIFY PRODUCT", (dialogInterface, i) -> {
            String productName = productNameInput.getText().toString();
            String productAmount = productAmountInput.getText().toString();
            String productPrefix = productPrefixInput.getText().toString();
            String productCalories = productCaloriesInput.getText().toString();
            if (productName.isEmpty() || productAmount.isEmpty() || productPrefix.isEmpty() || productCalories.isEmpty()) {
                View contextView = this.findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(
                        contextView,
                        "Couldn't save modified version to database\nPlease make sure you fill every field",
                        Snackbar.LENGTH_LONG
                );
                snackbar.show();
            } else {
                calorieProduct.setProductName(productName);
                calorieProduct.setProductAmount(Integer.parseInt(productAmount));
                calorieProduct.setAmountPrefix(productPrefix);
                calorieProduct.setCalorieAmount(Integer.parseInt(productCalories));

                editProduct(calorieProduct);
                rebuildTable();
            }
        });
        editProductPopup.show();
    }
}