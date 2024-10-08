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
      input: "STAGE",
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
      input: "STAGE",
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
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[0]._id
          ),
          stages: inspectionMethodsResult[0].stages.map((stage) => ({
            stage: stage.stage,
            name: stage.name,
            max: null,
            images: [],
          })),
        },
        {
          index: 1,
          id: generateUuid(),
          name: "Facade",
          group: "Wooden",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[0]._id
          ),
          stages: inspectionMethodsResult[0].stages.map((stage) => ({
            stage: stage.stage,
            name: stage.name,
            max: null,
            images: [],
          })),
        },
        {
          index: 2,
          id: generateUuid(),
          name: "Roof",
          group: "Concrete",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[1]._id
          ),
          stages: inspectionMethodsResult[1].stages.map((stage, index) => ({
            stage: stage.stage,
            name: stage.name,
            max: index === 0 ? 25 : null,
            images: [],
          })),
        },
        {
          index: 3,
          id: generateUuid(),
          name: "Facade",
          group: "Concrete",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[1]._id
          ),
          stages: inspectionMethodsResult[1].stages.map((stage, index) => ({
            stage: stage.stage,
            name: stage.name,
            max: index === 0 ? 25 : null,
            images: [],
          })),
        },
        {
          index: 4,
          id: generateUuid(),
          name: "Roof",
          group: "Steel",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[2]._id
          ),
          stages: inspectionMethodsResult[2].stages.map((stage, index) => ({
            stage: stage.stage,
            name: stage.name,
            max: index === 0 ? 25 : index === 1 ? 50 : null,
            images: [],
          })),
        },
        {
          index: 5,
          id: generateUuid(),
          name: "Facade",
          group: "Steel",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[2]._id
          ),
          stages: inspectionMethodsResult[2].stages.map((stage, index) => ({
            stage: stage.stage,
            name: stage.name,
            max: index === 0 ? 25 : index === 1 ? 50 : null,
            images: [],
          })),
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
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[0]._id
          ),
          stages: inspectionMethodsResult[0].stages.map((stage) => ({
            stage: stage.stage,
            naam: stage.name,
            max: null,
            images: [],
          })),
        },
        {
          index: 1,
          id: generateUuid(),
          name: "Facade",
          group: "Wooden",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[0]._id
          ),
          stages: inspectionMethodsResult[0].stages.map((stage) => ({
            stage: stage.stage,
            name: stage.name,
            max: null,
            images: [],
          })),
        },
        {
          index: 2,
          id: generateUuid(),
          name: "Roof",
          group: "Concrete",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[1]._id
          ),
          stages: inspectionMethodsResult[1].stages.map((stage) => ({
            stage: stage.stage,
            name: stage.name,
            max: null,
            images: [],
          })),
        },
        {
          index: 3,
          id: generateUuid(),
          name: "Facade",
          group: "Concrete",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[1]._id
          ),
          stages: inspectionMethodsResult[1].stages.map((stage) => ({
            stage: stage.stage,
            name: stage.name,
            max: null,
            images: [],
          })),
        },
        {
          index: 4,
          id: generateUuid(),
          name: "Roof",
          group: "Steel",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[2]._id
          ),
          stages: inspectionMethodsResult[2].stages.map((stage) => ({
            stage: stage.stage,
            name: stage.name,
            max: null,
            images: [],
          })),
        },
        {
          index: 5,
          id: generateUuid(),
          name: "Facade",
          group: "Steel",
          inspectionMethod: new DBRef(
            "inspection_method",
            inspectionMethodsResult[2]._id
          ),
          stages: inspectionMethodsResult[2].stages.map((stage) => ({
            stage: stage.stage,
            name: stage.name,
            max: null,
            images: [],
          })),
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
