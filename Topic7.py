class Material:
    def __init__(self, name, price_per_unit, stock):
        self.name = name
        self.price_per_unit = price_per_unit
        self.stock = stock

    def update_stock(self, quantity):
        self.stock += quantity

    def reduce_stock(self, quantity):
        if self.stock >= quantity:
            self.stock -= quantity
            return True
        return False

    def __str__(self):
        return f"{self.name} - Price: {self.price_per_unit}/unit - Stock: {self.stock} units"


class Order:
    def __init__(self, order_id, material_name, quantity, price_per_unit, priority):
        self.order_id = order_id
        self.material_name = material_name
        self.quantity = quantity
        self.total_price = price_per_unit * quantity
        self.priority = priority  

    def __str__(self):
        return f"Order ID: {self.order_id}, Material: {self.material_name}, Quantity: {self.quantity}, Total Price: {self.total_price}, Priority: {self.priority}"


class ConstructionSystem:
    def __init__(self):
        self.materials = {}
        self.orders = []

    def add_material(self, name, price_per_unit, stock):
        if name not in self.materials:
            self.materials[name] = Material(name, price_per_unit, stock)
        else:
            self.materials[name].update_stock(stock)

    def view_materials(self):
        if not self.materials:
            print("No materials available.")
        else:
            for material in self.materials.values():
                print(material)

    def place_order(self, order_id, material_name, quantity, priority):
        if material_name not in self.materials:
            print(f"Material '{material_name}' not found.")
            return
        material = self.materials[material_name]
        if material.reduce_stock(quantity):
            order = Order(order_id, material_name, quantity, material.price_per_unit, priority)
            self.orders.append(order)
            print(f"Order placed successfully: {order}")
        else:
            print(f"Insufficient stock for '{material_name}'. Available: {material.stock} units.")

    def view_orders(self):
        if not self.orders:
            print("No orders placed yet.")
        else:
            for order in self.orders:
                print(order)

    def bubble_sort_by_priority(self):
        n = len(self.orders)
        for i in range(n):
            for j in range(0, n-i-1):
                if self.orders[j].priority > self.orders[j+1].priority:
                    self.orders[j], self.orders[j+1] = self.orders[j+1], self.orders[j]
        print("\nOrders Sorted by Priority (Ascending):")
        for order in self.orders:
            print(order)

system = ConstructionSystem()


system.add_material("Cement", 50, 100)
system.add_material("Steel", 200, 50)
system.add_material("Bricks", 5, 1000)
system.add_material("Sand", 20, 500)


print("Available Materials:")
system.view_materials()

system.place_order(1, "Cement", 10, priority=3)
system.place_order(2, "Steel", 5, priority=1)
system.place_order(3, "Bricks", 100, priority=2)


print("\nOrders Before Sorting:")
system.view_orders()

system.bubble_sort_by_priority()


print("\nOrders After Sorting:")
system.view_orders()
