console.log("mongo-init.js script is running");

const generateUuid = () => {
  const s4 = () => {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  };

  return s4() + s4() + s4() + s4();
};

const createInspectionMethods = async (db) => {
  return await db.inspection_method.insertMany([
    {
      name: "QuickScan",
      input: "STAGE",
      calculationMethod: "NEN2767",
      _metadata: {
        created: new Date(),
        createdBy: null,
        updated: new Date(),
        updatedBy: null,
      },
    },
    {
      name: "URL2005",
      input: "PERCENTAGE",
      calculationMethod: null,
      _metadata: {
        created: new Date(),
        createdBy: null,
        updated: new Date(),
        updatedBy: null,
      },
    },
    {
      name: "CRI Method",
      input: "PERCENTAGE",
      calculationMethod: null,
      _metadata: {
        created: new Date(),
        createdBy: null,
        updated: new Date(),
        updatedBy: null,
      },
    },
  ]);
};

const createInspectionList = async (db, inspectionMethodInsertResult) => {
  return await db.inspection_list.insertMany([
    {
      name: "RGS+ NEN_2767",
      status: "DEFINITIVE",
      items: [
        {
          index: 0,
          value: {
            id: generateUuid(),
            name: "Roof",
            group: "Wooden",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["0"]
            ),
          },
        },
        {
          index: 1,
          value: {
            id: generateUuid(),
            name: "Facade",
            group: "Wooden",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["0"]
            ),
          },
        },
        {
          index: 2,
          value: {
            id: generateUuid(),
            name: "Roof",
            group: "Concrete",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["1"]
            ),
          },
        },
        {
          index: 3,
          value: {
            id: generateUuid(),
            name: "Facade",
            group: "Concrete",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["1"]
            ),
          },
        },
        {
          index: 4,
          value: {
            id: generateUuid(),
            name: "Roof",
            group: "Steel",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["2"]
            ),
          },
        },
        {
          index: 5,
          value: {
            id: generateUuid(),
            name: "Facade",
            group: "Steel",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["2"]
            ),
          },
        },
      ],
      labels: [
        {
          id: generateUuid(),
          index: 0,
          name: "Construction year",
          group: "General",
          features: [
            {
              index: 0,
              name: "1990",
            },
            {
              index: 1,
              name: "2000",
            },
            {
              index: 2,
              name: "2010",
            },
            {
              index: 3,
              name: "2020",
            },
          ],
        },
        {
          id: generateUuid(),
          index: 1,
          name: "Construction material",
          group: "General",
          features: [
            {
              index: 0,
              name: "Brick",
            },
            {
              index: 1,
              name: "Concrete",
            },
            {
              index: 2,
              name: "Steel",
            },
            {
              index: 3,
              name: "Wood",
            },
          ],
        },
      ],
      _metadata: {
        created: new Date(),
        createdBy: null,
        updated: new Date(),
        updatedBy: null,
      },
    },
    {
      name: "VAC v1",
      status: "CONCEPT",
      items: [
        {
          index: 0,
          value: {
            id: generateUuid(),
            name: "Roof",
            group: "Wooden",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["0"]
            ),
          },
        },
        {
          index: 1,
          value: {
            id: generateUuid(),
            name: "Facade",
            group: "Wooden",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["0"]
            ),
          },
        },
        {
          index: 2,
          value: {
            id: generateUuid(),
            name: "Roof",
            group: "Concrete",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["1"]
            ),
          },
        },
        {
          index: 3,
          value: {
            id: generateUuid(),
            name: "Facade",
            group: "Concrete",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["1"]
            ),
          },
        },
        {
          index: 4,
          value: {
            id: generateUuid(),
            name: "Roof",
            group: "Steel",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["2"]
            ),
          },
        },
        {
          index: 5,
          value: {
            id: generateUuid(),
            name: "Facade",
            group: "Steel",
            category: "SERIOUS",
            stages: [],
            inspectionMethod: new DBRef(
              "inspection_method",
              inspectionMethodInsertResult.insertedIds["2"]
            ),
          },
        },
      ],
      labels: [],
      _metadata: {
        created: new Date(),
        createdBy: null,
        updated: new Date(),
        updatedBy: null,
      },
    },
  ]);
};

createInspectionMethods(db).then(
  (inspectionMethodInsertResult) => {
    createInspectionList(db, inspectionMethodInsertResult).then(
      (inspectionListInsertResult) => {
        console.log("Inspection list created");
      },
      (error) => {
        console.log("Error creating inspection list", error);
      }
    );
  },
  (error) => {
    console.log("Error creating inspection methods", error);
  }
);
