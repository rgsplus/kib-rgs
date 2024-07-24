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
      stages: [
        {
          stage: 1,
          name: "Stage 1",
        },
        {
          stage: 2,
          name: "Stage 2",
        },
        {
          stage: 3,
          name: "Stage 3",
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
      name: "URL2005",
      input: "PERCENTAGE",
      calculationMethod: null,
      stages: [
        {
          stage: 1,
          name: "Stage 1",
        },
        {
          stage: 5,
          name: "Stage 2",
        },
        {
          stage: 10,
          name: "Stage 3",
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
      name: "CRI Method",
      input: "PERCENTAGE",
      calculationMethod: null,
      stages: [
        {
          stage: 1,
          name: "Stage 1",
        },
        {
          stage: 2,
          name: "Stage 2",
        },
        {
          stage: 3,
          name: "Stage 3",
        },
        {
          stage: 4,
          name: "Stage 4",
        },
        {
          stage: 5,
          name: "Stage 5",
        },
        {
          stage: 6,
          name: "Stage 6",
        },
        {
          stage: 7,
          name: "Stage 7",
        },
        {
          stage: 8,
          name: "Stage 8",
        },
        {
          stage: 9,
          name: "Stage 9",
        },
        {
          stage: 10,
          name: "Stage 10",
        },
      ],
      _metadata: {
        created: new Date(),
        createdBy: null,
        updated: new Date(),
        updatedBy: null,
      },
    },
  ]);
};

const createInspectionList = async (db, inspectionMethodsResult) => {
  return await db.inspection_list.insertMany([
    {
      name: "RGS+ NEN_2767",
      status: "DEFINITIVE",
      items: [
        {
          index: 0,
          id: generateUuid(),
          name: "Roof",
          group: "Wooden",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[0]._id
          ),
          stages: inspectionMethodsResult[0].stages.map((stage) => ({
            stage: stage.stage,
            naam: stage.name,
            max: null,
            image: null,
          })),
        },
        {
          index: 1,
          id: generateUuid(),
          name: "Facade",
          group: "Wooden",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[0]._id
          ),
          stages: inspectionMethodsResult[0].stages.map((stage) => ({
            stage: stage.stage,
            naam: stage.name,
            max: null,
            image: null,
          })),
        },
        {
          index: 2,
          id: generateUuid(),
          name: "Roof",
          group: "Concrete",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[1]._id
          ),
          stages: inspectionMethodsResult[1].stages.map((stage, index) => ({
            stage: stage.stage,
            naam: stage.name,
            max: index === 0 ? 25 : null,
            image: null,
          })),
        },
        {
          index: 3,
          id: generateUuid(),
          name: "Facade",
          group: "Concrete",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[1]._id
          ),
          stages: inspectionMethodsResult[1].stages.map((stage, index) => ({
            stage: stage.stage,
            naam: stage.name,
            max: index === 0 ? 25 : null,
            image: null,
          })),
        },
        {
          index: 4,
          id: generateUuid(),
          name: "Roof",
          group: "Steel",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[2]._id
          ),
          stages: inspectionMethodsResult[2].stages.map((stage, index) => ({
            stage: stage.stage,
            naam: stage.name,
            max: index === 0 ? 25 : index === 1 ? 50 : null,
            image: null,
          })),
        },
        {
          index: 5,
          id: generateUuid(),
          name: "Facade",
          group: "Steel",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[2]._id
          ),
          stages: inspectionMethodsResult[2].stages.map((stage, index) => ({
            stage: stage.stage,
            naam: stage.name,
            max: index === 0 ? 25 : index === 1 ? 50 : null,
            image: null,
          })),
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
          id: generateUuid(),
          name: "Roof",
          group: "Wooden",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[0]._id
          ),
          stages: inspectionMethodsResult[0].stages.map((stage) => ({
            stage: stage.stage,
            naam: stage.name,
            max: null,
            image: null,
          })),
        },
        {
          index: 1,
          id: generateUuid(),
          name: "Facade",
          group: "Wooden",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[0]._id
          ),
          stages: inspectionMethodsResult[0].stages.map((stage) => ({
            stage: stage.stage,
            naam: stage.name,
            max: null,
            image: null,
          })),
        },
        {
          index: 2,
          id: generateUuid(),
          name: "Roof",
          group: "Concrete",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[1]._id
          ),
          stages: inspectionMethodsResult[1].stages.map((stage) => ({
            stage: stage.stage,
            naam: stage.name,
            max: null,
            image: null,
          })),
        },
        {
          index: 3,
          id: generateUuid(),
          name: "Facade",
          group: "Concrete",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[1]._id
          ),
          stages: inspectionMethodsResult[1].stages.map((stage) => ({
            stage: stage.stage,
            naam: stage.name,
            max: null,
            image: null,
          })),
        },
        {
          index: 4,
          id: generateUuid(),
          name: "Roof",
          group: "Steel",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[2]._id
          ),
          stages: inspectionMethodsResult[2].stages.map((stage) => ({
            stage: stage.stage,
            naam: stage.name,
            max: null,
            image: null,
          })),
        },
        {
          index: 5,
          id: generateUuid(),
          name: "Facade",
          group: "Steel",
          category: "SIGNIFICANT",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[2]._id
          ),
          stages: inspectionMethodsResult[2].stages.map((stage) => ({
            stage: stage.stage,
            naam: stage.name,
            max: null,
            image: null,
          })),
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

const getInspectionMethods = async (db) => {
  return await db.inspection_method.find({}).toArray();
};

const executeScript = async (db) => {
  const inspectionMethodsResult = await createInspectionMethods(db);
  const inspectionMethods = await getInspectionMethods(db);
  const inspectionListResult = await createInspectionList(
    db,
    inspectionMethods
  );
};

executeScript(db);
