
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


class TreeNode:
    def __init__(self, material):
        self.material = material
        self.left = None
        self.right = None


class MaterialBinaryTree:
    def __init__(self):
        self.root = None

    def insert(self, material):
        def _insert_recursive(node, material):
            if node is None:
                return TreeNode(material)
            if material.name < node.material.name:
                node.left = _insert_recursive(node.left, material)
            elif material.name > node.material.name:
                node.right = _insert_recursive(node.right, material)
            return node

        self.root = _insert_recursive(self.root, material)

    def search(self, name):
        def _search_recursive(node, name):
            if node is None or node.material.name == name:
                return node
            if name < node.material.name:
                return _search_recursive(node.left, name)
            return _search_recursive(node.right, name)

        return _search_recursive(self.root, name)

    def in_order_traversal(self):
        materials = []

        def _in_order_recursive(node):
            if node:
                _in_order_recursive(node.left)
                materials.append(node.material)
                _in_order_recursive(node.right)

        _in_order_recursive(self.root)
        return materials


class ConstructionSystem:
    def __init__(self):
        self.material_tree = MaterialBinaryTree()

    def add_material(self, name, price_per_unit, stock):
        material = self.material_tree.search(name)
        if material:
            material.material.update_stock(stock)
        else:
            new_material = Material(name, price_per_unit, stock)
            self.material_tree.insert(new_material)

    def view_materials(self):
        materials = self.material_tree.in_order_traversal()
        if not materials:
            print("No materials available.")
        else:
            for material in materials:
                print(material)

    def place_order(self, name, quantity):
        material_node = self.material_tree.search(name)
        if not material_node:
            print(f"Material '{name}' not found.")
            return
        if material_node.material.reduce_stock(quantity):
            print(f"Order placed successfully: {quantity} units of {name}.")
        else:
            print(f"Insufficient stock for '{name}'. Available: {material_node.material.stock} units.")


system = ConstructionSystem()

system.add_material("Cement", 50, 100)
system.add_material("Steel", 200, 50)
system.add_material("Bricks", 5, 1000)
system.add_material("Sand", 20, 500)

print("\nAvailable Materials:")
system.view_materials()

print("\nPlacing Orders:")
system.place_order("Cement", 20)
system.place_order("Steel", 60)
system.place_order("Bricks", 200)
system.place_order("Wood", 10)

print("\nAvailable Materials After Orders:")
system.view_materials()
