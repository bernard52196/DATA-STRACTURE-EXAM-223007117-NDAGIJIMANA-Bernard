import datetime

class DoublyLinkedListNode:
    def __init__(self, data):
        self.data = data
        self.prev = None
        self.next = None

class DoublyLinkedList:
    def __init__(self):
        self.head = self.tail = None

    def append(self, data):
        new_node = DoublyLinkedListNode(data)
        if not self.head:
            self.head = self.tail = new_node
        else:
            self.tail.next = new_node
            new_node.prev = self.tail
            self.tail = new_node

    def remove(self, data):
        current = self.head
        while current:
            if current.data == data:
                if current.prev: current.prev.next = current.next
                if current.next: current.next.prev = current.prev
                if current == self.head: self.head = current.next
                if current == self.tail: self.tail = current.prev
                return True
            current = current.next
        return False

    def display(self, reverse=False):
        current = self.tail if reverse else self.head
        while current:
            print(current.data, end=" -> ")
            current = current.prev if reverse else current.next
        print("None")

class Stack:
    def __init__(self):
        self.stack = []

    def push(self, item): self.stack.append(item)
    def display(self): print("Stack Content:", self.stack)

class Material:
    def __init__(self, name, price, stock):
        self.name, self.price, self.stock = name, price, stock

    def update_stock(self, qty): self.stock -= qty
    def is_available(self, qty): return self.stock >= qty

class Order:
    def __init__(self, material, qty):
        self.material, self.qty = material, qty
        self.total = material.price * qty
        self.date = datetime.datetime.now()

    def __repr__(self):
        return f"Order({self.material.name}, {self.qty}, Total: {self.total})"

class OrderSystem:
    def __init__(self):
        self.materials, self.orders, self.delivered, self.stack = [], DoublyLinkedList(), DoublyLinkedList(), Stack()

    def add_material(self, name, price, stock):
        self.materials.append(Material(name, price, stock))

    def view_materials(self):
        print("\nAvailable Materials:\n====================")
        for i, m in enumerate(self.materials, 1):
            print(f"{i}. {m.name} - Price: {m.price}, Stock: {m.stock}")

    def place_order(self, idx, qty):
        material = self.materials[idx - 1]
        if material.is_available(qty):
            order = Order(material, qty)
            self.orders.append(order)
            material.update_stock(qty)
            self.stack.push(f"Order placed: {order}")
            print(f"Order placed: {order}")
        else:
            print(f"Insufficient stock for {material.name}.")

    def deliver_order(self):
        if self.orders.head:
            order = self.orders.head.data
            self.orders.remove(order)
            self.delivered.append(order)
            print(f"Order delivered: {order}")
        else:
            print("No pending orders to deliver.")



    def display_orders(self, delivered=False):
        
        print("\nDelivered Orders:" if delivered else "\nPending Orders:")
        print("Forward:")
        (self.delivered if delivered else self.orders).display()
        print("Backward:")
        (self.delivered if delivered else self.orders).display(reverse=True)

    def view_stack(self): 
        print("\nStack after Delivery:")
        self.stack.display()

system = OrderSystem()
system.add_material("Cement", 50, 100)
system.add_material("Steel", 200, 50)

system.view_materials()
system.place_order(1, 10)
system.place_order(2, 5)

system.display_orders()
system.view_stack()

system.deliver_order()
system.display_orders(delivered=True)

