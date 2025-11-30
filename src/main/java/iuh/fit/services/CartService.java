package iuh.fit.services;

import iuh.fit.dtos.CheckoutRequest;
import iuh.fit.dtos.cart.AddToCartRequest;
import iuh.fit.dtos.cart.CartDTO;
import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.entities.User;

public interface CartService {
    CartDTO getCartByUser(User currentUser);

    CartDTO addOrUpdateItem(User currentUser, AddToCartRequest request);

    CartDTO updateItemQuantity(User currentUser, Integer productId, Integer quantity);

    CartDTO removeItem(User currentUser, Integer productId);

    OrderDTO checkout(User currentUser, CheckoutRequest request);
}
